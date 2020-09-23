<template>
    <div class="container">
        <a-list item-layout="horizontal" :data-source="paths">
            <a-list-item slot="renderItem" slot-scope="item">
                <a-list-item-meta :description="item.description">
                    <div slot="title">
                        <div class="name">{{ item.name }}</div>
                        <a-button class="button" size="small" shape="circle" :icon="item.visible ? 'eye' : 'eye-invisible'" @click="_onVisibleButtonClick(item)" />
                        <a-button class="button" size="small" shape="circle" icon="bg-colors" @click="_onColorChanged(item)" />
                        <a-button class="button" size="small" shape="circle" icon="close" @click="_onCloseButtonClick(item)" />
                    </div>
                </a-list-item-meta>
            </a-list-item>
        </a-list>
        <ColorPicker ref="colorPicker" @onPicked="_onColorPicked" />
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
import ColorPicker from '@/components/colorpicker/ColorPicker';

export default {
    name: 'UAVPathList',
    components: {
        ColorPicker
    },
    computed: {
        paths() {
            return this.$store.state.uav.dataViewPaths;
        }
    },
    methods: {
        _onVisibleButtonClick(item) {
            this.$store.commit('switchPathVisible', item);
        },
        _onColorChanged(item) {
            this.$refs.colorPicker.show(item.color, item);
        },
        _onCloseButtonClick(item) {
            this.$store.commit('removePath', item);
        },
        _onColorPicked(color, item) {
            this.$store.commit('setPathColor', { item, color });
        }
    }
};
</script>
