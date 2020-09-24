<template>
    <div class="container">
        <div style="display: inline-block; vertical-align: middle;"><a-switch checked-children="显示高度" un-checked-children="忽略高度" v-model="showAltitude" /></div>
        <div style="display: inline-block; width: 120px; vertical-align: middle; margin: 0 8px;"><a-slider :default-value="altitude" :min="100" :max="1000" :setp="100" :disabled="showAltitude" v-model="altitude" /></div>
    </div>
</template>

<style scoped>
.container {
    width: 240px;
    height: 60px;
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
        },
        altitude: {
            get() {
                return this.$store.state.uav.altitude;
            },
            set(newVal) {
                this.$store.commit('setAltitude', newVal);
            }
        }
    }
};
</script>
