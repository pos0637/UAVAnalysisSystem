<template>
    <a-card :bordered="false">
        <div>{{ '测试任务:' + name }}</div>
        <div class="taskinfo">
            <div v-for="(item, i) in taskItem" :key="i" class="taskItem" @click="selectItem(item)">
                <div>{{ item.title }}</div>
            </div>
        </div>

        <j-modal class="" :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" fullscreen @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
            <a-spin :spinning="confirmLoading">
                <component :is="component" @callBack="setResult" :taskId="id" />
            </a-spin>
        </j-modal>
    </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less';
import { httpAction } from '@/api/manage';
// import { mixinDevice } from '@/utils/mixin';

export default {
    name: 'UavTaskInfo',
    components: {},
    props: ['taskId', 'taskName'],
    data() {
        return {
            name: this.taskName,
            dictOptions: {},
            title: '',
            width: 300,
            confirmLoading: false,
            visible: false,
            selectType: '0',
            resultData: null,
            taskItem: [
                {
                    type: '1',
                    title: '最大起飞质量',
                    path: '/task/item/1',
                    componentName: 'TOMForm',
                },
                {
                    type: '2',
                    title: '最大作业半径',
                    path: '/task/item/1',
                    componentName: 'RadiusForm',
                },
                {
                    type: '3',
                    title: '最大飞行海拔高度',
                    path: '/task/item/1',
                    componentName: 'AltitudeForm',
                },
                {
                    type: '4',
                    title: '最大平飞速度',
                    path: '/task/item/1',
                    componentName: 'PFSForm',
                },
                {
                    type: '5',
                    title: '最大爬升速度',
                    path: '/task/item/1',
                    componentName: 'CSForm',
                },
                {
                    type: '6',
                    title: '高度保持性能',
                    path: '/task/item/1',
                    componentName: 'HRPForm',
                },
                {
                    type: '7',
                    title: '速度保持性能',
                    path: '/task/item/1',
                    componentName: 'SRPForm',
                },
                {
                    type: '8',
                    title: '空载悬停',
                    path: '/task/item/1',
                    componentName: 'NLHForm',
                },
                {
                    type: '9',
                    title: '空载平飞',
                    path: '/task/item/1',
                    componentName: 'NLPFForm',
                },
                {
                    type: '10',
                    title: '满载悬停',
                    path: '/task/item/1',
                    componentName: 'FLHForm',
                },
                {
                    type: '11',
                    title: '满载平飞',
                    path: '/task/item/1',
                    componentName: 'FLPFForm',
                },
                {
                    type: '12',
                    title: '定点悬停',
                    path: '/task/item/1',
                    componentName: 'FPHForm',
                },
                {
                    type: '13',
                    title: '定位导航',
                    path: '/task/item/1',
                    componentName: 'FPNForm',
                },
                {
                    type: '14',
                    title: '航迹精度',
                    path: '/task/item/1',
                    componentName: 'TAForm',
                },
                {
                    type: '15',
                    title: '抗风能力',
                    path: '/task/item/1',
                    componentName: 'WRForm',
                },
            ],
            component: null,
            id: this.taskId,
        };
    },
    created() {},
    watch: {
        taskName(val) {
            this.name = val;
        },
        taskId(val) {
            this.id = val;
        },
    },
    computed: {},
    // 导航离开该组件的对应路由时调用[可以访问组件实例 `this`]
    beforeRouteLeave(to, from, next) {
        // 销毁组件，避免通过vue-router再次进入时，仍是上次的history缓存的状态
        this.$destroy(true);
        next();
    },
    methods: {
        selectItem(item) {
            this.visible = true;
            this.title = item.title;
            this.selectType = item.type;
            this.component = () => {
                return import(`./modules/${item.componentName}`);
            };
        },
        close() {
            this.visible = false;
            this.$emit('close');
        },
        handleOk() {
            if (this.resultData === null) {
                this.$message.warning('结论为空');
                return;
            }
            this.confirmLoading = true;
            const data = {
                taskId: this.id,
                type: this.selectType,
                content: JSON.stringify(this.resultData),
            };
            httpAction('/uav/uavTaskItem/add', data, 'post')
                .then((res) => {
                    if (res.success) {
                        this.$message.success(res.message);
                        this.$emit('ok');
                        this.close();
                    } else {
                        this.$message.warning(res.message);
                    }
                })
                .finally(() => {
                    this.confirmLoading = false;
                });
        },
        submitCallback() {
            this.$emit('ok');
            this.visible = false;
        },
        handleCancel() {
            this.close();
        },
        setResult(e) {
            this.resultData = e;
            // alert(JSON.stringify(e));
        },
    },
};
</script>
<style scoped>
@import '~@assets/less/common.less';
.taskinfo {
    padding: 10px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
}
.taskItem {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 17%;
    height: 80px;
    /* padding: 40px; */
    border: 1px solid #292424aa;
    text-align: center;
    margin: 15px;
    cursor: pointer;
    border-radius: 20px;
    align-content: center;
}
</style>