<template>
    <div style="width: 100%; height: 100%; position: relative;">
        <UAVMap />
        <div class="toolbox">
            <UAVToolbox />
            <div class="select-path"><Dropdown ref="paths" :width="120" url="/uav/datav/paths" /><a-button style="margin-left: 8px;" @click="_onAddPathButtonClick">添加</a-button></div>
            <UAVPathList />
        </div>
    </div>
</template>

<style scoped>
.toolbox {
    position: absolute;
    left: 20px;
    top: 20px;
    display: flex;
    flex-flow: column;
}

.select-path {
    display: flex;
    flex-direction: row;
    width: 240px;
    height: 40px;
    background-color: rgba(255, 255, 255, 0.7);
}
</style>

<script>
import Dropdown from '@/components/dropdown/Dropdown';
import UAVToolbox from '@/components/business/uavToolbox/UAVToolbox';
import UAVPathList from '@/components/business/uavPathList/UAVPathList';
import UAVMap from '@/components/business/uavMap/UAVMap';
import { getAction } from '@/api/manage';

export default {
    components: {
        Dropdown,
        UAVToolbox,
        UAVPathList,
        UAVMap
    },
    methods: {
        _onAddPathButtonClick() {
            const id = this.$refs.paths.getSelected();
            if (id !== null) {
                getAction(`/uav/datav/path?id=${id}`).then(res => {
                    if (res.success) {
                        this.$store.commit('addPath', res.result);
                    }
                });
            }
        }
    }
};
</script>
