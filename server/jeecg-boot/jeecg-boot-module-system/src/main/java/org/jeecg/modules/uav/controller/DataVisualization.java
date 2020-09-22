package org.jeecg.modules.uav.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.uav.entity.UavPath;
import org.jeecg.modules.uav.service.IUavPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

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

    @GetMapping(value = "/path")
    public Result<?> getPath(@RequestParam(name = "id", required = true) String id) {
        UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return Result.error("未找到对应数据");
        }

        String filePath = uploadpath + File.separator + uavPath.getFile();
        File file = new File(filePath);
        if (!file.exists()) {
            return Result.error("未找到对应数据");
        }

        List<org.jeecg.modules.uav.vo.UavPath.Point> list = new LinkedList<org.jeecg.modules.uav.vo.UavPath.Point>();
        CsvReader reader = CsvUtil.getReader();
        CsvData data = reader.read(FileUtil.file(filePath));
        List<CsvRow> rows = data.getRows();
        for (CsvRow csvRow : rows) {
            List<String> cols = csvRow.getRawList();
            if (cols.get(0).equals("$GPGGA")) {
                // GPS定位信息
                list.add(new org.jeecg.modules.uav.vo.UavPath.Point(
                        DateUtil.parse(cols.get(1), "hhmmss.SSS"),
                        Convert.toFloat(cols.get(4)),
                        Convert.toFloat(cols.get(2)),
                        Convert.toFloat(cols.get(9))
                ));
            }
        }

        org.jeecg.modules.uav.vo.UavPath path = new org.jeecg.modules.uav.vo.UavPath();
        path.setId(uavPath.getId());
        path.setName(uavPath.getName());
        path.setDescription(uavPath.getDescription());
        path.setPoints(list);

        return Result.ok(path);
    }
}
