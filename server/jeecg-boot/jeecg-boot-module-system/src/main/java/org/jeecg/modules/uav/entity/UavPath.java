package org.jeecg.modules.uav.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 无人机轨迹数据
 * @Author: jeecg-boot
 * @Date: 2020-12-18
 * @Version: V1.0
 */
@Data
@TableName("uav_path")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "uav_path对象", description = "无人机轨迹数据")
public class UavPath implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
    /**
     * 中心点经度
     */
    @Excel(name = "中心点经度", width = 15)
    @ApiModelProperty(value = "中心点经度")
    private java.lang.Double centerLongitude;
    /**
     * 中心点纬度
     */
    @Excel(name = "中心点纬度", width = 15)
    @ApiModelProperty(value = "中心点纬度")
    private java.lang.Double centerLatitude;
    /**
     * 中心点高程
     */
    @Excel(name = "中心点纬度", width = 15)
    @ApiModelProperty(value = "中心点高程")
    private java.lang.Double centerAltitude;
    /**
     * 轨迹数据文件
     */
    @Excel(name = "轨迹数据文件", width = 15)
    @ApiModelProperty(value = "轨迹数据文件")
    private java.lang.String file;
}
