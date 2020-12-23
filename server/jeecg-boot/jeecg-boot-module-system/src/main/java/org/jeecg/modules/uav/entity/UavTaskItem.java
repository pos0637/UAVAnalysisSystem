package org.jeecg.modules.uav.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 测试任务项
 * @Author: jeecg-boot
 * @Date: 2020-12-22
 * @Version: V1.0
 */
@Data
@TableName("uav_task_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "uav_task_item对象", description = "测试任务项")
public class UavTaskItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 测试类型
     */
    @Excel(name = "测试类型", width = 15, dicCode = "task_test_type")
    @Dict(dicCode = "task_test_type")
    @ApiModelProperty(value = "测试类型")
    private String type;
    /**
     * 测试任务主键
     */
    @Excel(name = "测试任务主键", width = 15, dictTable = "uav_task", dicText = "name", dicCode = "id")
    @Dict(dictTable = "uav_task", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "测试任务主键")
    private String taskId;
    /**
     * 测试内容/结果
     */
    @Excel(name = "测试内容/结果", width = 15)
    @ApiModelProperty(value = "测试内容/结果")
    private String content;
}
