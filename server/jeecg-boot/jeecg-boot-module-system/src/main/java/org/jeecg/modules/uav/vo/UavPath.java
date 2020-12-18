package org.jeecg.modules.uav.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 路径
 *
 * @author Alex
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class UavPath implements Serializable {
    /**
     * 索引
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否可见
     */
    private boolean visible = true;

    /**
     * 颜色
     */
    private String color = "#FF000080";

    /**
     * 中心点经度
     */
    private double centerLongitude;

    /**
     * 中心点纬度
     */
    private double centerLatitude;

    /**
     * 轨迹点
     */
    private List<Point> points;

    /**
     * 轨迹点
     */
    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Point implements Serializable {
        /**
         * 时间
         */
        @Excel(name = "采样时间", width = 15)
        private Date time;

        /**
         * 经度
         */
        @Excel(name = "经度", width = 15)
        private float lng;


        /**
         * 纬度
         */
        @Excel(name = "纬度", width = 15)
        private float lat;

        /**
         * 高程
         */
        @Excel(name = "高程", width = 15)
        private float alt;
    }
}
