<template>
    <div class="container">
        <a-list item-layout="horizontal" :data-source="paths">
            <a-list-item slot="renderItem" slot-scope="item">
                <a-list-item-meta :description="item.description">
                    <a slot="title"
                        ><a-icon type="line-chart" /> &nbsp;{{ item.name }}&nbsp;
                        <a-button shape="circle" :icon="item.visible ? 'eye' : 'eye-invisible'" @click="_onVisibleButtonClick(item)" />
                        <a-button shape="circle"></a-button><a-button shape="circle" icon="close" @click="_onCloseButtonClick(item)" />
                    </a>
                </a-list-item-meta>
            </a-list-item>
        </a-list>
    </div>
</template>

<style scoped>
.container {
    position: absolute;
    left: 20px;
    top: 60px;
    width: 200px;
    height: 500px;
    padding: 8px;
    background-color: rgba(255, 255, 255, 0.7);
    overflow-y: scroll;
}
</style>

<script>
export default {
    name: 'UAVPathList',
    computed: {
        paths() {
            return this.$store.state.uav.dataViewPaths;
        }
    },
    methods: {
        _onVisibleButtonClick(item) {
            this.$store.commit('switchPathVisible', item.id);
        },
        _onColorChanged(item) {},
        _onCloseButtonClick(item) {
            this.$store.commit('removePath', item.id);
        }
    }
};
</script>
