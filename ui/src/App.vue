<template>
    <a-layout class="layout">
        <a-layout-header>Header</a-layout-header>
        <a-layout-content>
            <a-row>
                <a-form layout="inline" :form="query">
                    <a-row type="flex" align="middle">
                        <a-col flex="120px">
                            <span>无人机数据名称:</span>
                        </a-col>
                        <a-col flex="auto">
                            <a-input placeholder="请输入无人机数据名称" style="width: 100%" />
                        </a-col>
                        <a-col flex="100px" style="text-align: center">
                            <a-button type="primary" html-type="submit">
                                查询
                            </a-button>
                        </a-col>
                    </a-row>
                </a-form>
            </a-row>
            <a-row>
                <a-table :columns="columns" :data-source="data">
                    <a slot="name" slot-scope="text">{{ text }}</a>
                    <span slot="customTitle">Name</span>
                    <span slot="tags" slot-scope="tags">
                        <a-tag v-for="tag in tags" :key="tag" :color="tag === '错误' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'">
                            {{ tag.toUpperCase() }}
                        </a-tag>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a>Invite 一 {{ record.name }}</a>
                        <a-divider type="vertical" />
                        <a>Delete</a>
                        <a-divider type="vertical" />
                        <a class="ant-dropdown-link"> More actions <a-icon type="down" /> </a>
                    </span>
                </a-table>
            </a-row>
        </a-layout-content>
        <a-layout-footer>Footer</a-layout-footer>
    </a-layout>
</template>

<style scoped>
.layout {
    width: 100%;
    height: 100%;
}
</style>

<script>
const columns = [
    {
        title: '名称',
        dataIndex: 'name',
        key: 'name'
    },
    {
        title: '测试地点',
        dataIndex: 'address',
        key: 'address'
    },
    {
        title: '测试时间',
        dataIndex: 'time',
        key: 'time'
    },
    {
        title: '标签',
        key: 'tags',
        dataIndex: 'tags',
        scopedSlots: { customRender: 'tags' }
    },
    {
        title: '操作',
        key: 'action',
        scopedSlots: { customRender: 'action' }
    }
];

const data = [
    {
        key: '1',
        name: '浦东国际机场1',
        address: '浦东国际机场',
        tags: ['正常', '测试']
    },
    {
        key: '2',
        name: '浦东国际机场2',
        address: '浦东国际机场',
        tags: ['错误']
    },
    {
        key: '3',
        name: '浦东国际机场3',
        address: '浦东国际机场',
        tags: ['正常']
    }
];

export default {
    data() {
        return {
            data,
            columns
        };
    }
};
</script>
