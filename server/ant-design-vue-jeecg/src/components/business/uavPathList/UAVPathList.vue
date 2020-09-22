<template>
    <div class="container">
        <a-list item-layout="horizontal" :data-source="paths">
            <a-list-item slot="renderItem" slot-scope="item">
                <a-list-item-meta :description="item.description">
                    <div slot="title">
                        <div class="name">{{ item.name }}</div>
                        <a-button class="button" size="small" shape="circle" :icon="item.visible ? 'eye' : 'eye-invisible'" @click="_onVisibleButtonClick(item)" />
                        <a-button class="button" size="small" shape="circle" icon="close" />
                        <a-button class="button" size="small" shape="circle" icon="close" @click="_onCloseButtonClick(item)" />
                    </div>
                </a-list-item-meta>
            </a-list-item>
        </a-list>
    </div>
</template>

<style scoped>
.container {
    width: 240px;
    height: 500px;
    padding: 8px;
    background-color: rgba(255, 255, 255, 0.7);
    overflow-y: scroll;
}

.name {
    display: inline-block;
    width: 120px;
    margin: 0 4px;
    overflow: hidden;
    vertical-align: middle;
    font-size: 1.3rem;
}

.button {
    margin: 0 2px;
    vertical-align: middle;
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
            this.$store.commit('switchPathVisible', item);
        },
        _onColorChanged(item) {},
        _onCloseButtonClick(item) {
            this.$store.commit('removePath', item);
        }
    }
};
</script>
