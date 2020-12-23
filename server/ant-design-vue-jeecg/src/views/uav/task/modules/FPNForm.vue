<template>
    <a-card :bordered="false">
        <a-form @submit="handleSubmit" :form="form">
            <a-form-item label="起点(x/y/z)" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['x1', { rules: [{ required: true, message: '请输入起点x' }] }]" name="x1" placeholder="请输入起点x" />
            </a-form-item>
            <a-form-item label=" " :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['y1', { rules: [{ required: true, message: '请输入起点y' }] }]" name="y1" placeholder="请输入起点y" />
            </a-form-item>
            <a-form-item label=" " :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['z1', { rules: [{ required: true, message: '请输入起点z' }] }]" name="z1" placeholder="请输入起点z" />
            </a-form-item>
            <a-form-item label="终点(x/y/z)" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['x2', { rules: [{ required: true, message: '请输入终点x' }] }]" name="x2" placeholder="请输入终点x" />
            </a-form-item>
            <a-form-item label=" " :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['y2', { rules: [{ required: true, message: '请输入终点y' }] }]" name="y2" placeholder="请输入终点y" />
            </a-form-item>
            <a-form-item label=" " :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['z2', { rules: [{ required: true, message: '请输入终点z' }] }]" name="z2" placeholder="请输入终点z" />
            </a-form-item>
            <a-form-item label="导航精度" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number disabled style="width: 100%" name="value" placeholder="自动计算" />
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
/**
 * 定位导航
 */
export default {
    name: 'FPNForm',
    components: {},
    data() {
        return {
            form: this.$form.createForm(this),
        };
    },
    created() {},
    computed: {},
    methods: {
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFields((err, values) => {
                if (!err) {
                    // eslint-disable-next-line no-console
                    console.log('Received values of form: ', values);
                }
            });
        },
    },
};
</script>
<style scoped>
</style>