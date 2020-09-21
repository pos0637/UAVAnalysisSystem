<template>
    <div style="width: 100%; height: 100%; position: relative;">
        {{ msg }}
        <div id="map-container"></div>
        <UAVPathList />
    </div>
</template>

<style scoped>
#map-container {
    width: 100%;
    height: 100%;
}
</style>

<script>
import UAVPathList from '@/components/business/uavPathList/UAVPathList';
import { getAction } from '@/api/manage';
import { getPaths } from '@/mock/path';
import { hexToRgba } from '@/utils/color';

export default {
    components: {
        UAVPathList
    },
    data() {
        return {
            msg: '',
            map: null,
            points3D: null,
            lines3D: null
        };
    },
    created() {
        this.hello();
    },
    mounted() {
        this._initializeMap();
        this._updatePaths();
    },
    destroyed() {
        this.map && this.map.destroy();
    },
    methods: {
        hello() {
            var url = '/uav/datav/hello';
            getAction(url).then(res => {
                if (res.success) {
                    this.msg = res.result;
                }
            });
        },
        _initializeMap() {
            if (this.map !== null) {
                return;
            }

            // 创建地图组件
            this.map = new AMap.Map('map-container', {
                resizeEnable: true, // 是否监控地图容器尺寸变化
                zoom: 17, // 初始化地图层级
                center: [121.814324, 31.126484], // 初始化地图中心点
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

            // 添加3D点集合
            this.points3D = new AMap.Object3D.Points();
            this.points3D.transparent = true;

            // 添加3D线集合
            this.lines3D = new AMap.Object3D.Line();
            this.lines3D.transparent = true;

            // 添加3D图层
            const object3Dlayer = new AMap.Object3DLayer({ zIndex: 110, opacity: 1 });
            this.map.add(object3Dlayer);
            object3Dlayer.add(this.points3D);
            object3Dlayer.add(this.lines3D);

            this.map.on('complete', () => {
                console.debug('地图加载完成');
            });
        },
        _updatePaths() {
            const paths = getPaths();
            for (const path of paths) {
                for (const point of path.points) {
                    this._add3DPoint(point, hexToRgba(path.color));
                }

                for (let i = 1; i < path.points.length; i++) {
                    this._add3DLine(path.points[i - 1], path.points[i], hexToRgba(path.color));
                }
            }
        },
        _add3DPoint(point, color) {
            const geometry = this.points3D.geometry;
            const p = this._lnglatToG20(point);
            geometry.vertices.push(p.x, p.y, p.z);
            geometry.pointSizes.push(10);
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
            const result = this.map.lngLatToGeodeticCoord([lnglat.lng, lnglat.lat]);
            result.x = AMap.Util.format(result.x, 3);
            result.y = AMap.Util.format(result.y, 3);
            result.z = lnglat.alt;

            return result;
        }
    }
};
</script>
