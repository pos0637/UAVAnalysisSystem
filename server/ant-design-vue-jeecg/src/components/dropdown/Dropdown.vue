<template>
    <a-dropdown :style="style">
        <a-menu slot="overlay" @click="_onMenuClick">
            <a-menu-item v-for="item in list" :key="item.id" :title="item.name">
                {{ item.name }}
            </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> {{ selected }}<a-icon type="down" /> </a-button>
    </a-dropdown>
</template>

<script>
import { getAction } from '@/api/manage';

export default {
    name: 'Dropdown',
    props: {
        width: {
            type: Number,
            default: 120
        },
        url: String
    },
    data() {
        return {
            style: { width: `${this.width}px` },
            list: [],
            selected: '请选择轨迹',
            selectedId: null
        };
    },
    mounted() {
        this._getList();
    },
    methods: {
        getSelected() {
            return this.selectedId;
        },
        _getList() {
            getAction(this.url).then(res => {
                if (res.success) {
                    this.list = res.result;
                }
            });
        },
        _onMenuClick(e) {
            this.selected = e.item.title;
            this.selectedId = e.key;
        }
    }
};
</script>
