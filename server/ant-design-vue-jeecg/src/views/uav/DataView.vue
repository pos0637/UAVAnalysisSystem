<template>
    <div style="width: 100%; height: 100%;">
        {{ msg }}
        <UAVMap />
    </div>
</template>

<script>
import UAVMap from '@/components/business/uavMap/UAVMap';
import { getAction } from '@/api/manage';

export default {
    components: {
        UAVMap
    },
    data() {
        return {
            msg: null
        };
    },
    methods: {
        hello() {
            var url = '/uav/datav/hello';
            getAction(url).then(res => {
                if (res.success) {
                    this.msg = res.result;
                }
            });
        }
    }
};
</script>
