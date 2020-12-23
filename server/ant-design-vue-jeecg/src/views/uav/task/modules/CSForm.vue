<template>
    <a-card :bordered="false">
        <a-form @submit="handleSubmit" :form="form">
            <a-tabs default-active-key="1">
                <a-tab-pane key="1" tab="第一次测试值">
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file1', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file1')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a1', { rules: [{ required: true, message: '请输入起点' }] }]" name="a1" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b1', { rules: [{ required: true, message: '请输入终点' }] }]" name="b2" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>

                <a-tab-pane key="2" tab="第二次测试值" force-render>
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file2', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file2')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a2', { rules: [{ required: true, message: '请输入起点' }] }]" name="a2" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b2', { rules: [{ required: true, message: '请输入终点' }] }]" name="b2" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>

                <a-tab-pane key="3" tab="第三次测试值" force-render>
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file3', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file3')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a3', { rules: [{ required: true, message: '请输入起点' }] }]" name="a3" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b3', { rules: [{ required: true, message: '请输入终点' }] }]" name="b3" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>
            </a-tabs>
            <a-form-item label="最大爬升速率" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number disabled style="width: 100%" :value="value" name="value" placeholder="自动计算" />
            </a-form-item>
            <a-row style="text-align: center">
                <a-col>
                    <a-button htmlType="submit" type="primary">自动分析</a-button>
                </a-col>
            </a-row>
        </a-form>
    </a-card>
</template>

<script>
import JUpload from '@/components/jeecg/JUpload';
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
/**
 * 最大爬升速率
 */
export default {
    name: 'CSForm',
    components: { JUpload },
    props: ['taskId'],
    watch: {
        taskId(val) {
            this.id = val;
        },
    },
    data() {
        return {
            labelCol: { lg: { span: 6 }, sm: { span: 6 } },
            wrapperCol: { lg: { span: 15 }, sm: { span: 15 } },
            form: this.$form.createForm(this),
            validatorRules: {
                file: {
                    rules: [{ required: true, message: '请输入轨迹数据文件!' }],
                },
            },
            value: 0,
            id: this.taskId,
        };
    },
    created() {},
    computed: {},
    mounted() {
        this.init();
    },
    methods: {
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFields((err, values) => {
                if (!err) {
                    this.analysis(values);
                }
            });
        },
        analysis(data) {
            httpAction(`/uav/analysis/cs`, data, 'post')
                .then((res) => {
                    if (res.success) {
                        if (res.result !== null) {
                            this.value = res.result.toFixed(2);
                            data.value = this.value;
                            this.$emit('callBack', data);
                        }
                    } else {
                        this.$message.warning(res.message);
                    }
                })
                .finally(() => {});
        },
        init() {
            const taskId = this.id;
            httpAction(`/uav/uavTaskItem/queryByTypeAndTask?taskId=${taskId}&type=5`, {}, 'get')
                .then((res) => {
                    if (res.success) {
                        const data = res.result;
                        const result = JSON.parse(data.content);
                        this.$nextTick(() => {
                            this.value = result.value;
                            this.form.setFieldsValue(result);
                        });
                    }
                })
                .finally(() => {});
        },
        handleExportPointCloud(path) {
            const file = this.form.getFieldValue(path);
            if (file) {
                window.location.href = `${window._CONFIG['domianURL']}/uav/datav/exportPointCloudWithFile?filePath=${file}`;
            }
        },
    },
};
</script>
<style scoped>
</style>