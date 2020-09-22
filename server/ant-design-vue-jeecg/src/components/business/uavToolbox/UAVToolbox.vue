<template>
    <div class="container">
        <a-switch checked-children="显示高度" un-checked-children="忽略高度" v-model="showAltitude" />
    </div>
</template>

<style scoped>
.container {
    position: absolute;
    left: 20px;
    top: 20px;
    width: 200px;
    height: 20px;
    padding: 8px;
    background-color: rgba(255, 255, 255, 0.7);
}
</style>

<script>
export default {
    name: 'UAVToolbox',
    computed: {
        showAltitude: {
            get() {
                return this.$store.state.uav.showAltitude;
            },
            set(newVal) {
                this.$store.commit('switchShowAltitude');
            }
        }
    }
};
</script>
