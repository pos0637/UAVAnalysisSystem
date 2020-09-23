<template>
    <a-modal v-model="visible" title="请选择轨迹颜色" :width="220" :closable="false" :bodyStyle="{ padding: '0px' }" @ok="_handleOk">
        <Sketch :value="color" @input="_updateValue" />
    </a-modal>
</template>

<style scoped></style>

<script>
import { Sketch } from 'vue-color';

export default {
    name: 'ColorPicker',
    components: {
        Sketch
    },
    data() {
        return {
            visible: false,
            color: '#000000',
            userData: null
        };
    },
    methods: {
        show(color, userData) {
            this.visible = true;
            this.color = color;
            this.userData = userData;
        },
        _updateValue(newVal) {
            this.color = newVal.hex8;
        },
        _handleOk() {
            this.visible = false;
            this.$emit('onPicked', this.color, this.userData);
        }
    }
};
</script>
