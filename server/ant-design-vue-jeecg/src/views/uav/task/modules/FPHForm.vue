<template>
    <a-form @submit="handleSubmit" :form="form">
        <a-card :bordered="false">
            <a-tabs default-active-key="1">
                <a-tab-pane key="1" tab="第一次测试值">
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file1', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file1')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="原点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a1', { rules: [{ required: true, message: '请输入原点' }] }]" name="a1" placeholder="请输入原点" />
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b1', { rules: [{ required: true, message: '请输入起点' }] }]" name="b1" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['c1', { rules: [{ required: true, message: '请输入终点' }] }]" name="c1" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>
                <a-tab-pane key="2" tab="第二次测试值" force-render>
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file2', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file2')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="原点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a2', { rules: [{ required: true, message: '请输入原点' }] }]" name="a2" placeholder="请输入原点" />
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b2', { rules: [{ required: true, message: '请输入起点' }] }]" name="b2" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['c2', { rules: [{ required: true, message: '请输入终点' }] }]" name="c2" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>
                <a-tab-pane key="3" tab="第三次测试值" force-render>
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file3', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file3')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="原点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a3', { rules: [{ required: true, message: '请输入原点' }] }]" name="a3" placeholder="请输入原点" />
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b3', { rules: [{ required: true, message: '请输入起点' }] }]" name="b3" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['c3', { rules: [{ required: true, message: '请输入终点' }] }]" name="c3" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>
                <a-tab-pane key="4" tab="第四次测试值" force-render>
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file4', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file4')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="原点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a4', { rules: [{ required: true, message: '请输入原点' }] }]" name="a4" placeholder="请输入原点" />
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b4', { rules: [{ required: true, message: '请输入起点' }] }]" name="b4" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['c4', { rules: [{ required: true, message: '请输入终点' }] }]" name="c4" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>
                <a-tab-pane key="5" tab="第五次测试值" force-render>
                    <a-form-item label="轨迹数据文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-upload v-decorator="['file5', validatorRules.file]" :trigger-change="true"></j-upload>
                        <a @click="handleExportPointCloud('file5')">导出点云</a>
                    </a-form-item>
                    <a-form-item label="原点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['a5', { rules: [{ required: true, message: '请输入原点' }] }]" name="a5" placeholder="请输入原点" />
                    </a-form-item>
                    <a-form-item label="起点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['b5', { rules: [{ required: true, message: '请输入起点' }] }]" name="b5" placeholder="请输入起点" />
                    </a-form-item>
                    <a-form-item label="终点序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number style="width: 100%" v-decorator="['c5', { rules: [{ required: true, message: '请输入终点' }] }]" name="c5" placeholder="请输入终点" />
                    </a-form-item>
                </a-tab-pane>
            </a-tabs>
            <a-form-item label="水平距离均值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number disabled style="width: 100%" :value="lAvg" name="value" placeholder="自动计算" />
            </a-form-item>
            <a-form-item label="水平距离标准差" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number disabled style="width: 100%" :value="lStd" name="value" placeholder="自动计算" />
            </a-form-item>
            <a-form-item label="垂直距离均值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number disabled style="width: 100%" :value="hAvg" name="value" placeholder="自动计算" />
            </a-form-item>
            <a-form-item label="垂直距离标准差" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number disabled style="width: 100%" :value="hStd" name="value" placeholder="自动计算" />
            </a-form-item>
            <a-row style="text-align: center">
                <a-col>
                    <a-button htmlType="submit" type="primary">自动分析</a-button>
                </a-col>
            </a-row>
        </a-card>
    </a-form>
</template>

<script>
import JUpload from '@/components/jeecg/JUpload';
import { httpAction } from '@/api/manage';

/**
 * 定位悬停
 */
export default {
    name: 'FPHForm',
    components: { JUpload },
    props: ['taskId'],
    watch: {
        taskId(val) {
            this.id = val;
        },
    },
    data() {
        return {
            form: this.$form.createForm(this),
            labelCol: {
                lg: { span: 6 },
                sm: { span: 6 },
            },
            wrapperCol: {
                lg: { span: 15 },
                sm: { span: 15 },
            },
            validatorRules: {
                file: {
                    rules: [{ required: true, message: '请输入轨迹数据文件!' }],
                },
            },
            lAvg: 0,
            lStd: 0,
            hAvg: 0,
            hStd: 0,
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
            httpAction(`/uav/analysis/fph`, data, 'post')
                .then((res) => {
                    if (res.success) {
                        if (res.result !== null) {
                            this.lAvg = res.result.lAvg.toFixed(2);
                            this.lStd = res.result.lStd.toFixed(2);
                            this.hAvg = res.result.hAvg.toFixed(2);
                            this.hStd = res.result.hStd.toFixed(2);
                            data.lAvg = this.lAvg;
                            data.lStd = this.lStd;
                            data.hAvg = this.hAvg;
                            data.hStd = this.hStd;
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
            httpAction(`/uav/uavTaskItem/queryByTypeAndTask?taskId=${taskId}&type=12`, {}, 'get')
                .then((res) => {
                    if (res.success) {
                        const data = res.result;
                        const result = JSON.parse(data.content);
                        this.$nextTick(() => {
                            this.lAvg = result.lAvg;
                            this.lStd = result.lStd;
                            this.hAvg = result.hAvg;
                            this.hStd = result.hStd;
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