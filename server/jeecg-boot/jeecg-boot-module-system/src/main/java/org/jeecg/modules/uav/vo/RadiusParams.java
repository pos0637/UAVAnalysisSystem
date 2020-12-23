package org.jeecg.modules.uav.vo;

import lombok.Data;

/**
 * 最大半径计算参数
 *
 * @author liujianning
 */
@Data
public class RadiusParams {

    /**
     * 第一次测试 A点索引
     */
    private int a1;

    /**
     * 第二次测试 A点索引
     */
    private int a2;

    /**
     * 第三次测试 A点索引
     */
    private int a3;

    /**
     * 第一次测试 b点索引
     */
    private int b1;

    /**
     * 第二次测试 b点索引
     */
    private int b2;

    /**
     * 第三次测试 b点索引
     */
    private int b3;

    /**
     * 第一次测试 c点索引
     */
    private int c1;

    /**
     * 第二次测试 c点索引
     */
    private int c2;

    /**
     * 第三次测试 c点索引
     */
    private int c3;

    /**
     * 第一次测试轨迹文件名称
     */
    private String file1;

    /**
     * 第二次测试轨迹文件名称
     */
    private String file2;

    /**
     * 第三次测试轨迹文件名称
     */
    private String file3;
}
