<template>
    <a-card :bordered="false">
        <a-form @submit="handleSubmit" :form="form">
            <a-form-item label="Ho Max (标准大气压下最大飞行高度)" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['h1', { rules: [{ required: true, message: '请输入最大飞行高度' }] }]" name="h1" placeholder="请输入最大飞行高度" />
            </a-form-item>
            <a-form-item label="H (起飞点海拔)" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input-number style="width: 100%" v-decorator="['h2', { rules: [{ required: true, message: '请输入起飞点海拔' }] }]" name="h2" placeholder="请输入起飞点海拔" />
            </a-form-item>
            <a-form-item label="最大飞行海拔高度" :labelCol="{ lg: { span: 6 }, sm: { span: 6 } }" :wrapperCol="{ lg: { span: 15 }, sm: { span: 15 } }">
                <a-input disabled style="width: 100%" :value="h3" placeholder="自动计算" suffix="m" />
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
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';

/**
 *  海拔高度
 */
export default {
    name: 'AltitudeForm',
    components: {},
    props: ['taskId'],
    data() {
        return {
            form: this.$form.createForm(this),
            h3: 0,
            id: this.taskId,
        };
    },
    created() {},
    computed: {},
    watch: {
        taskId(val) {
            this.id = val;
        },
    },
    mounted() {
        this.init();
    },
    methods: {
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFields((err, values) => {
                if (!err) {
                    this.h3 = values.h1 - values.h2;
                    const data = {
                        h1: values.h1,
                        h2: values.h2,
                        h3: this.h3,
                    };
                    this.$emit('callBack', data);
                }
            });
        },
        init() {
            const taskId = this.id;
            httpAction(`/uav/uavTaskItem/queryByTypeAndTask?taskId=${taskId}&type=3`, {}, 'get')
                .then((res) => {
                    if (res.success) {
                        const data = res.result;
                        const result = JSON.parse(data.content);
                        this.$nextTick(() => {
                            this.form.setFieldsValue(pick(result, 'h1', 'h2'));
                        });
                        this.h3 = result.h3;
                    }
                })
                .finally(() => {});
        },
    },
};
</script>
<style scoped>
</style>