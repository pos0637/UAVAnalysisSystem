package org.jeecg.modules.uav.service;

import org.jeecg.modules.uav.vo.*;

import java.util.Map;

public interface ICalcService {

    /**
     * 最大作业半径
     *
     * @param params
     * @return
     */
    Double getMaxRadius(RadiusParams params);

    /**
     * 最大平飞速度
     *
     * @param params
     * @return
     */
    Double getMaxPFS(PFSParams params);

    /**
     * 最大爬升速率
     *
     * @param params
     * @return
     */
    Double getMaxCS(CSParams params);

    /**
     * 高度保持性能
     *
     * @param params
     * @return
     */
    Double getHRP(HRPParams params);

    /**
     * 速度保持性能
     *
     * @param params
     * @return
     */
    Double getSRP(HRPParams params);

    /**
     * 定位悬停
     *
     * @param params
     * @return
     */
    Map<String, Double> getFPH(FPHParams params);

    /**
     * 空载悬停
     *
     * @param params
     * @return
     */
    Long getNLH(NLHParams params);

    /**
     * 空载平飞
     *
     * @param params
     * @return
     */
    Long getNLP(NLHParams params);

    /**
     * 满载悬停
     *
     * @param params
     * @return
     */
    Long getFLH(NLHParams params);

    /**
     * 满载平飞
     *
     * @param params
     * @return
     */
    Long getFLP(NLHParams params);
}
