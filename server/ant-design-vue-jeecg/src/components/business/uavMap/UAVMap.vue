<template>
    <div id="map-container" ref="mapContainer">
        <div class="hint" :style="{ left: hintLeft, top: hintTop }" v-if="lastPointInfo !== null">
            轨迹:{{ this.lastPointInfo.path.name }}<br />
            时间:{{ this.lastPointInfo.point.time }}<br />
            经度:{{ this.lastPointInfo.point.lng }}<br />
            纬度:{{ this.lastPointInfo.point.lat }}<br />
            高程:{{ this.lastPointInfo.point.alt }}<br />
        </div>
    </div>
</template>

<style scoped>
#map-container {
    width: 100%;
    height: 100%;
    position: relative;
}

.hint {
    position: absolute;
    width: 120;
    height: 50;
    z-index: 999;
    padding: 8px;
    background-color: rgba(255, 255, 255, 0.7);
}
</style>

<script>
import * as Three from 'three';
import { hexToRgba } from '@/utils/color';
import { axios } from '@/utils/request';

/**
 * 高度中心
 */
const altitudeCenter = 500.0;

/**
 * 地球赤道半径(公里)
 */
const EARTH_RADIUS = 6378.137;

function deg2Rad(degree) {
    return (degree * Math.PI) / 180.0;
}

function getDistance(lng1, lat1, lng2, lat2) {
    const rad1 = deg2Rad(lat1);
    const rad2 = deg2Rad(lat2);
    const a = rad1 - rad2;
    const b = deg2Rad(lng1) - deg2Rad(lng2);

    let distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)));
    distance = distance * EARTH_RADIUS;
    distance = Math.round(distance * 10000.0) / 10000.0;

    return distance * 1000;
}

export default {
    name: 'UAVMap',
    data() {
        return {
            timer: null,
            hintLeft: '0px',
            hintTop: '0px',
            map: null,
            object3Dlayer: null,
            points3D: null,
            lines3D: null,
            lastPoint: null,
            lastPointInfo: null
        };
    },
    computed: {
        paths() {
            const model = this.$store.state.uav;
            return this._interpolate(model.dataViewPaths, model.interpolateMode, model.interpolationScaler);
        },
        showAltitude() {
            return this.$store.state.uav.showAltitude;
        },
        altitudeScaler() {
            return this.$store.state.uav.altitudeScaler;
        },
        center() {
            const center = this.$store.state.uav.center;
            return [center[0] / 100.0, center[1] / 100.0];
        }
    },
    watch: {
        '$store.state.uav.dataViewPaths': {
            handler: function(newVal) {
                this._clearPaths();
                this._updatePaths();
            },
            deep: true
        },
        '$store.state.uav.showAltitude': function() {
            this._clearPaths();
            this._updatePaths();
        },
        '$store.state.uav.altitudeScaler': function() {
            this._clearPaths();
            this._updatePaths();
        },
        '$store.state.uav.interpolateMode': function() {
            this._clearPaths();
            this._updatePaths();
        },
        '$store.state.uav.interpolationScaler': function() {
            this._clearPaths();
            this._updatePaths();
        },
        '$store.state.uav.center': function() {
            this._setCenter();
        }
    },
    mounted() {
        this.timer = setInterval(async () => {
            if (window.amapLoaded) {
                clearInterval(this.timer);
                await this._initializeMap();
                this._updatePaths();
            }
        }, 500);
    },
    beforeDestroy() {
        this.timer && clearInterval(this.timer);
        this.map && this.map.destroy();
    },
    methods: {
        _initializeMap() {
            return new Promise((resolve, reject) => {
                this.map && this.map.destroy();

                // 创建地图组件
                this.map = new AMap.Map('map-container', {
                    resizeEnable: true, // 是否监控地图容器尺寸变化
                    zoom: 17, // 初始化地图层级
                    center: this.center, // 初始化地图中心点
                    rotateEnable: true,
                    pitchEnable: true,
                    pitch: 80,
                    rotation: -15,
                    viewMode: '3D', // 开启3D视图,默认为关闭
                    buildingAnimation: true, // 楼块出现是否带动画
                    expandZoomRange: true,
                    zooms: [3, 20]
                });

                // 创建控件
                this.map.addControl(
                    new AMap.ControlBar({
                        showZoomBar: true,
                        showControlButton: true,
                        position: {
                            right: '10px',
                            top: '10px'
                        }
                    })
                );

                this.map.on('complete', () => {
                    console.debug('地图加载完成');

                    if (typeof AMap.Object3DLayer !== 'undefined') {
                        // 添加3D图层
                        this.object3Dlayer = new AMap.Object3DLayer({ zIndex: 110, opacity: 1 });
                        this.map.add(this.object3Dlayer);

                        resolve();
                    }
                });

                this.map.on('mousemove', e => {
                    const pixel = e.pixel;
                    const px = new AMap.Pixel(pixel.x, pixel.y);
                    const obj = this.map.getObject3DByContainerPos(px, [this.object3Dlayer], false);
                    if (obj !== null && obj.object === this.points3D) {
                        if (this.lastPoint !== obj.index) {
                            this.hintLeft = `${e.pixel.x}px`;
                            this.hintTop = `${e.pixel.y}px`;
                            this.lastPoint = obj.index;
                            this.lastPointInfo = this._getPointInfo(this.lastPoint);
                        }
                    } else {
                        this.lastPoint = null;
                        this.lastPointInfo = null;
                    }
                });
            });
        },
        _clearPaths() {
            if (this.object3Dlayer !== null) {
                if (this.points3D !== null) {
                    this.object3Dlayer.remove(this.points3D);
                    this.points3D = null;
                }

                if (this.lines3D !== null) {
                    this.object3Dlayer.remove(this.lines3D);
                    this.lines3D = null;
                }
            }
        },
        _updatePaths() {
            // 添加3D点集合
            this.points3D = new AMap.Object3D.Points();
            this.points3D.transparent = true;
            this.object3Dlayer.add(this.points3D);
            this.points3D.borderColor = [0.6, 0.8, 1, 1];
            this.points3D.borderWeight = 3;

            // 添加3D线集合
            this.lines3D = new AMap.Object3D.Line();
            this.lines3D.transparent = true;
            this.object3Dlayer.add(this.lines3D);

            const paths = this.paths;
            for (const path of paths) {
                if (!path.visible) {
                    continue;
                }

                // 计算最大最小高度
                const minHight = path.points.reduce((prev, cur) => {
                    return prev.alt < cur.alt ? prev : cur;
                }, -9999.0).alt;
                const maxHight = path.points.reduce((prev, cur) => {
                    return prev.alt > cur.alt ? prev : cur;
                }, 0.0).alt;

                for (const point of path.points) {
                    this._add3DPoint(point, this._calcColor(path.color, minHight, maxHight, point.alt));
                }

                for (let i = 1; i < path.points.length; i++) {
                    this._add3DLine(path.points[i - 1], path.points[i], this._calcColor(path.color, minHight, maxHight, path.points[i].alt));
                }
            }
        },
        _setCenter() {
            this.map.setCenter(this.center);
        },
        _add3DPoint(point, color) {
            const geometry = this.points3D.geometry;
            const p = this._lnglatToG20(point);
            geometry.vertices.push(p.x, p.y, p.z);
            geometry.pointSizes.push(16);
            geometry.vertexColors.push(color.r, color.g, color.b, color.a);
        },
        _add3DLine(point1, point2, color) {
            const geometry = this.lines3D.geometry;
            let p = this._lnglatToG20(point1);
            geometry.vertices.push(p.x, p.y, p.z);
            geometry.vertexColors.push(color.r, color.g, color.b, color.a);

            p = this._lnglatToG20(point2);
            geometry.vertices.push(p.x, p.y, p.z);
            geometry.vertexColors.push(color.r, color.g, color.b, color.a);
        },
        _lnglatToG20(lnglat) {
            const result = this.map.lngLatToGeodeticCoord([lnglat.lng / 100.0, lnglat.lat / 100.0]);
            result.x = AMap.Util.format(result.x, 3);
            result.y = AMap.Util.format(result.y, 3);
            result.z = this.showAltitude ? this._calcAltitude(lnglat.alt, this.altitudeScaler) : -altitudeCenter;

            return result;
        },
        _calcAltitude(real, scaler) {
            return -((real - altitudeCenter) * scaler + altitudeCenter);
        },
        _getPointInfo(pointIndex) {
            const paths = this.paths;
            let i = 0;
            for (const path of paths) {
                if (!path.visible) {
                    continue;
                }

                for (let j = 0; j < path.points.length; i++, j++) {
                    if (i === pointIndex) {
                        return {
                            path: path,
                            point: path.points[j]
                        };
                    }
                }
            }

            return null;
        },
        _interpolate(paths, mode, scaler) {
            if (mode === 'none') {
                return paths;
            }

            const result = [];
            for (const path of paths) {
                // 生成插值点
                const points = [];
                for (const point of path.points) {
                    points.push(new Three.Vector3(point.lng, point.lat, point.alt));
                }

                const curve = mode === 'line' ? this._interpolateLine3(points, scaler) : this._interpolateSplineCurve3(points, scaler);

                // 生成新路径
                result.push({
                    ...path,
                    points: curve.map(p => {
                        return { lng: p.x, lat: p.y, alt: p.z };
                    })
                });
            }

            return result;
        },
        _interpolateLine3(points, scaler) {
            let result = [];
            for (let i = 1; i < points.length; i++) {
                result = result.concat(new Three.LineCurve3(points[i - 1], points[i]).getPoints(scaler));
            }

            return result;
        },
        _interpolateSplineCurve3(points, scaler) {
            return new Three.SplineCurve3(points).getPoints(points.length * scaler);
        },
        _calcColor(base, min, max, value) {
            const rgba = hexToRgba(base);
            const p = (value - min) / (max - min + 0.0001);
            rgba.r -= p * rgba.r;
            rgba.g -= p * rgba.g;
            rgba.b -= p * rgba.b;

            return rgba;
        },
        export(id) {
            const paths = this.paths;
            for (const path of paths) {
                if (path.id === id) {
                    let points = '';
                    for (const point of path.points) {
                        const x = getDistance(point.lng / 100.0, 0.0, path.centerLongitude / 100.0, 0.0);
                        const y = getDistance(0, point.lat / 100.0, 0.0, path.centerLatitude / 100.0);
                        points += `${x},${y},${point.alt}\r\n`;
                    }
                    this._exportFile(`${path.name}.xyz`, points);
                    return;
                }
            }
        },
        _exportFile(filename, content) {
            const element = document.createElement('a');
            element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
            element.setAttribute('download', filename);
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        }
    }
};
</script>
