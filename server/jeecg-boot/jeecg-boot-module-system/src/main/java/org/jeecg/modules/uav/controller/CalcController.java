package org.jeecg.modules.uav.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.uav.service.ICalcService;
import org.jeecg.modules.uav.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据分析接口
 *
 * @author liujianning
 */
@Api(tags = "测试任务")
@RestController
@RequestMapping("/uav/analysis")
@Slf4j
public class CalcController {

    @Autowired
    private ICalcService service;

    /**
     * 最大半径分析
     *
     * @param params 测试参数
     * @return 返回值
     */
    @PostMapping(value = "/radius")
    public Result<?> maxRadius(@RequestBody RadiusParams params) throws Exception {
        return Result.ok(service.getMaxRadius(params));
    }

    /**
     * 最大平飞速度
     *
     * @param params 测试参数
     * @return 返回值
     */
    @PostMapping(value = "/pfs")
    public Result<?> maxPFS(@RequestBody PFSParams params) throws Exception {
        return Result.ok(service.getMaxPFS(params));
    }

    /**
     * 最大爬升速率
     *
     * @param params 测试参数
     * @return 返回值
     */
    @PostMapping(value = "/cs")
    public Result<?> maxPFS(@RequestBody CSParams params) throws Exception {
        return Result.ok(service.getMaxCS(params));
    }

    /**
     * 高度保持性能
     *
     * @param params 测试参数
     * @return 返回值
     */
    @PostMapping(value = "/hrp")
    public Result hrp(@RequestBody HRPParams params) throws Exception {
        return Result.ok(service.getHRP(params));
    }

    /**
     * 速度保持性能
     *
     * @param params 测试参数
     * @return 返回值
     */
    @PostMapping(value = "/srp")
    public Result srp(@RequestBody HRPParams params) throws Exception {
        return Result.ok(service.getSRP(params));
    }

    /**
     * 定点悬停
     *
     * @param params 计算参数
     * @return 返回值
     */
    @PostMapping(value = "/fph")
    public Result fph(@RequestBody FPHParams params) throws Exception {
        return Result.ok(service.getFPH(params));
    }

    /**
     * 空载悬停
     *
     * @param params 计算参数
     * @return 返回值
     */
    @PostMapping(value = "/nlh")
    public Result nlh(@RequestBody NLHParams params) throws Exception {
        return Result.ok(service.getNLH(params));
    }

    /**
     * 空载平飞
     *
     * @param params 计算参数
     * @return 返回值
     */
    @PostMapping(value = "/nlp")
    public Result nlp(@RequestBody NLHParams params) throws Exception {
        return Result.ok(service.getNLP(params));
    }

    /**
     * 满载悬停
     *
     * @param params 计算参数
     * @return 返回值
     */
    @PostMapping(value = "/flh")
    public Result flh(@RequestBody NLHParams params) throws Exception {
        return Result.ok(service.getFLH(params));
    }

    /**
     * 满载平飞
     *
     * @param params 计算参数
     * @return 返回值
     */
    @PostMapping(value = "/flp")
    public Result flp(@RequestBody NLHParams params) throws Exception {
        return Result.ok(service.getFLP(params));
    }
}
