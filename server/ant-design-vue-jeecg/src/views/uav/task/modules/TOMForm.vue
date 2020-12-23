<template>
    <a-card :bordered="false">
        <a-form @submit="_handleSubmit" :form="form">
            <a-form-item label="最大起飞质量" :labelCol="{ lg: { span: 4 }, sm: { span: 4 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input style="width: 100%" v-decorator="['quality', { rules: [{ required: true, message: '请输入质量' }] }]" name="quality" placeholder="请输入质量" />
            </a-form-item>
            <a-form-item label="是否正常完成" :labelCol="{ lg: { span: 4 }, sm: { span: 4 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-select :value="value" style="width: 100px" @change="_change">
                    <a-select-option value="0">否</a-select-option>
                    <a-select-option value="1">是</a-select-option>
                </a-select>
            </a-form-item>

            <a-row style="text-align: center">
                <a-col>
                    <a-button htmlType="submit" type="primary">提交</a-button>
                </a-col>
            </a-row>
        </a-form>
    </a-card>
</template>

<script>
/**
 * 最大起飞质量
 */
import { httpAction } from '@/api/manage';

export default {
    name: 'TOMForm',
    components: {},
    props: ['taskId'],
    watch: {
        taskId(val) {
            this.id = val;
        },
    },
    data() {
        return {
            value: '0',
            form: this.$form.createForm(this),
            id: this.taskId,
        };
    },
    created() {},
    mounted() {
        this.init();
    },
    computed: {},
    methods: {
        _handleSubmit(e) {
            e.preventDefault();
            this.form.validateFields((err, values) => {
                if (!err) {
                    const data = {
                        type: 1,
                        value: this.value,
                        quality: values.quality,
                    };
                    this.$emit('callBack', data);
                }
            });
        },
        _change(v) {
            this.value = v;
        },
        init() {
            const taskId = this.id;
            httpAction(`/uav/uavTaskItem/queryByTypeAndTask?taskId=${taskId}&type=1`, {}, 'get')
                .then((res) => {
                    if (res.success) {
                        const data = res.result;
                        const result = JSON.parse(data.content);
                        this.value = result.value;
                        this.$nextTick(() => {
                            this.form.setFieldsValue(result);
                        });
                    }
                })
                .finally(() => {});
        },
    },
};
</script>
<style scoped>
</style>