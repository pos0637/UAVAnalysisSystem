<template>
    <baidu-map ref="bmap" class="map" ak="xYQGkcrw9Rd7V7mW8hWa4oSwXhIy9dx8" center="上海">
        <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']" anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>
        <bm-scale anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-scale>
        <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
        <bm-overview-map anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :isOpen="true"></bm-overview-map>
        <bm-polyline v-for="(path, index) in lines.paths" :key="index" :path="path.points" :stroke-color="path.color" :stroke-opacity="0.5"></bm-polyline>
    </baidu-map>
</template>

<style scoped>
.map {
    width: 100%;
    height: 100%;
}
</style>

<script>
import BaiduMap from 'vue-baidu-map/components/map/Map.vue';
import { BmMapType, BmScale, BmNavigation, BmOverviewMap, BmPolyline } from 'vue-baidu-map';

export default {
    name: 'Map',
    components: {
        BaiduMap,
        BmMapType,
        BmScale,
        BmNavigation,
        BmOverviewMap,
        BmPolyline
    },
    data() {
        return {
            lines: {
                editing: false,
                paths: [
                    {
                        color: '#00FF00',
                        points: [
                            {
                                lng: 121.814224,
                                lat: 31.156484
                            },
                            {
                                lng: 121.808547,
                                lat: 31.165754
                            },
                            {
                                lng: 121.796186,
                                lat: 31.184848
                            },
                            {
                                lng: 121.788353,
                                lat: 31.192015
                            },
                            {
                                lng: 121.771752,
                                lat: 31.194734
                            },
                            {
                                lng: 121.74969,
                                lat: 31.189791
                            },
                            {
                                lng: 121.754217,
                                lat: 31.179658
                            },
                            {
                                lng: 121.757523,
                                lat: 31.167794
                            }
                        ]
                    },
                    {
                        color: 'red',
                        points: [
                            {
                                lng: 121.814324,
                                lat: 31.126484
                            },
                            {
                                lng: 121.808447,
                                lat: 31.135754
                            },
                            {
                                lng: 121.796286,
                                lat: 31.174848
                            },
                            {
                                lng: 121.788453,
                                lat: 31.112015
                            },
                            {
                                lng: 121.771152,
                                lat: 31.154734
                            },
                            {
                                lng: 121.74569,
                                lat: 31.179791
                            },
                            {
                                lng: 121.756217,
                                lat: 31.139658
                            },
                            {
                                lng: 121.752523,
                                lat: 31.127794
                            }
                        ]
                    }
                ]
            }
        };
    },
    methods: {}
};
</script>
