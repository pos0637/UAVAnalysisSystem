<template>
    <div id="map-container" ref="mapContainer">
        <div class="hint" :style="{ left: hintLeft, top: hintTop }" v-if="lastPointInfo !== null">
            轨迹:{{ this.lastPointInfo.path.name }}<br />
            时间:{{ this.lastPointInfo.point.time }}<br />
            经度:{{ this.lastPointInfo.point.lng }}<br />
            纬度:{{ this.lastPointInfo.point.lat }}<br />
            高层:{{ this.lastPointInfo.point.alt }}<br />
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
import { hexToRgba } from '@/utils/color';

/**
 * 高度中心
 */
const altitudeCenter = 500.0;

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
            return this.$store.state.uav.dataViewPaths;
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
        '$store.state.uav.center': function() {
            this._setCenter();
        }
    },
    mounted() {
        this.timer = setInterval(() => {
            if (window.amapLoaded) {
                this._initializeMap();
                this._updatePaths();
                clearInterval(this.timer);
            }
        }, 500);
    },
    beforeDestroy() {
        this.timer && clearInterval(this.timer);
        this.map && this.map.destroy();
    },
    methods: {
        _initializeMap() {
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

            // 添加3D图层
            this.object3Dlayer = new AMap.Object3DLayer({ zIndex: 110, opacity: 1 });
            this.map.add(this.object3Dlayer);

            this.map.on('complete', () => {
                console.debug('地图加载完成');
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
            this.points3D = new AMap.Object3D.RoundPoints();
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

                for (const point of path.points) {
                    this._add3DPoint(point, hexToRgba(path.color));
                }

                for (let i = 1; i < path.points.length; i++) {
                    this._add3DLine(path.points[i - 1], path.points[i], hexToRgba(path.color));
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
        }
    }
};
</script>
