package org.jeecg.modules.uav.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.uav.service.IUavPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据可视化控制器
 *
 * @author Alex
 */
@RestController
@RequestMapping("/uav/datav")
@Slf4j
public class DataVisualization {
    @Autowired
    private IUavPathService uavPathService;

    @GetMapping(value = "/hello")
    public Result<String> hello() {
        Result<String> result = new Result<>();
        result.setResult("Hello World!");
        result.setSuccess(true);
        return result;
    }

    @GetMapping(value = "/paths")
    public Result<?> getPaths() {
        return Result.ok(uavPathService.list());
    }
}
