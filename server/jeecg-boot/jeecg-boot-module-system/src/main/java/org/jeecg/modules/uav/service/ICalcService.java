package org.jeecg.modules.uav.service;

import org.jeecg.modules.uav.vo.*;

import java.util.Map;

public interface ICalcService {

    /**
     * 最大作业半径
     *
     * @param params 计算参数
     * @return 最大作业半径
     */
    Double getMaxRadius(RadiusParams params) throws Exception;

    /**
     * 最大平飞速度
     *
     * @param params 计算参数
     * @return 最大平飞速度
     */
    Double getMaxPFS(PFSParams params) throws Exception;

    /**
     * 最大爬升速率
     *
     * @param params 计算参数
     * @return 最大爬升速率
     */
    Double getMaxCS(CSParams params) throws Exception;

    /**
     * 高度保持性能
     *
     * @param params 计算参数
     * @return 高度保持性能
     */
    Double getHRP(HRPParams params) throws Exception;

    /**
     * 速度保持性能
     *
     * @param params 计算参数
     * @return 速度保持性能
     */
    Double getSRP(HRPParams params) throws Exception;

    /**
     * 定位悬停
     *
     * @param params 计算参数
     * @return 定位悬停
     */
    Map<String, Double> getFPH(FPHParams params) throws Exception;

    /**
     * 空载悬停
     *
     * @param params 计算参数
     * @return 空载悬停
     */
    Long getNLH(NLHParams params) throws Exception;

    /**
     * 空载平飞
     *
     * @param params 计算参数
     * @return 空载平飞
     */
    Long getNLP(NLHParams params) throws Exception;

    /**
     * 满载悬停
     *
     * @param params 计算参数
     * @return 满载悬停
     */
    Long getFLH(NLHParams params) throws Exception;

    /**
     * 满载平飞
     *
     * @param params 计算参数
     * @return 满载平飞
     */
    Long getFLP(NLHParams params) throws Exception;
}
