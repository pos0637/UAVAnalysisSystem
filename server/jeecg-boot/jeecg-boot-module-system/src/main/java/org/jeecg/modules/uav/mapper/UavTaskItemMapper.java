package org.jeecg.modules.uav.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.uav.entity.UavTaskItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 测试任务项
 * @Author: jeecg-boot
 * @Date: 2020-12-22
 * @Version: V1.0
 */
public interface UavTaskItemMapper extends BaseMapper<UavTaskItem> {

    @Delete("DELETE FROM uav_task_item where type = #{type} and task_id = #{taskId}")
    void deleteByTypeAndId(@Param("type") String type, @Param("taskId") String taskId);
}
