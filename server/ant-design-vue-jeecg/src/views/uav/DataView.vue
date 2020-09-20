<template>
    <div style="width: 100%; height: 100%;">
        {{ msg }}
        <div id="map-container"></div>
    </div>
</template>

<style scoped>
#map-container {
    width: 100%;
    height: 100%;
}
</style>

<script>
import { getAction } from '@/api/manage';
export default {
    data() {
        return {
            msg: '',
            map: null
        };
    },
    created() {
        this.hello();
    },
    mounted() {
        this.initializeMap();
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
        initializeMap() {
            this.map = new AMap.Map('map-container', {
                resizeEnable: true, //是否监控地图容器尺寸变化
                zoom: 17, //初始化地图层级
                center: [116.397428, 39.90923], //初始化地图中心点
                rotateEnable: true,
                pitchEnable: true,
                pitch: 80,
                rotation: -15,
                viewMode: '3D', //开启3D视图,默认为关闭
                buildingAnimation: true, //楼块出现是否带动画
                expandZoomRange: true,
                zooms: [3, 20]
            });

            this.map.addControl(
                new AMap.ControlBar({
                    showZoomBar: false,
                    showControlButton: true,
                    position: {
                        right: '10px',
                        top: '10px'
                    }
                })
            );

            this.map.add(
                new AMap.Object3DLayer({
                    zIndex: 110,
                    opacity: 1
                })
            );

            this.map.on('complete', () => {
                console.debug('地图加载完成');
            });
        },
        _lnglatToG20(lnglat) {
            lnglat = this.map.lngLatToGeodeticCoord(lnglat);
            lnglat.x = AMap.Util.format(lnglat.x, 3);
            lnglat.y = AMap.Util.format(lnglat.y, 3);
            return lnglat;
        }
    }
};
</script>
