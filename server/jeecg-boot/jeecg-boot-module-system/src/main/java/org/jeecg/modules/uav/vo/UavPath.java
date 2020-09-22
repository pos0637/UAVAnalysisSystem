package org.jeecg.modules.uav.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
        private Date time;
        /**
         * 经度
         */
        private float lng;

        /**
         * 纬度
         */
        private float lat;

        /**
         * 高程
         */
        private float alt;
    }
}
