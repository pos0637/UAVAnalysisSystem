package org.jeecg.modules.uav.vo;

import lombok.Data;

@Data
public class HRPParams {

    /**
     * 第一次阶段 起点
     */
    private int a1;

    /**
     * 第一次阶段 终点
     */
    private int b1;

    /**
     * 第二次阶段 起点
     */
    private int a2;

    /**
     * 第二次阶段 终点
     */
    private int b2;

    /**
     * 第三次阶段 起点
     */
    private int a3;

    /**
     * 第三次阶段 终点
     */
    private int b3;

    /**
     * 第一次阶段 轨迹
     */
    private String file1;
}
