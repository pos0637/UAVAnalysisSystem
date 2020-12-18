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
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    /**
     * 地球赤道半径(公里)
     */
    private static final double EARTH_RADIUS = 6378.137;

    @Autowired
    private IUavPathService uavPathService;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    private static double deg2Rad(final double degree) {
        return degree * Math.PI / 180.0;
    }

    private static double getDistance(final double lng1, final double lat1, final double lng2, final double lat2) {
        final double rad1 = deg2Rad(lat1);
        final double rad2 = deg2Rad(lat2);
        final double a = rad1 - rad2;
        final double b = deg2Rad(lng1) - deg2Rad(lng2);

        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000.0) / 10000.0;

        return distance * 1000;
    }

    @GetMapping(value = "/paths")
    public Result<?> getPaths() {
        return Result.ok(uavPathService.list());
    }

    @GetMapping(value = "/path")
    public Result<?> getPath(@RequestParam(name = "id", required = true) String id) {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return Result.error("未找到对应数据");
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return Result.error("未找到对应数据");
        }

        final List<org.jeecg.modules.uav.vo.UavPath.Point> list = new LinkedList<>();
        final CsvReader reader = CsvUtil.getReader();
        final CsvData data = reader.read(FileUtil.file(filePath));
        final List<CsvRow> rows = data.getRows();
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

        final org.jeecg.modules.uav.vo.UavPath path = new org.jeecg.modules.uav.vo.UavPath();
        path.setId(uavPath.getId());
        path.setName(uavPath.getName());
        path.setDescription(uavPath.getDescription());
        path.setCenterLongitude(uavPath.getCenterLongitude());
        path.setCenterLatitude(uavPath.getCenterLatitude());
        path.setPoints(list);

        return Result.ok(path);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(@RequestParam(name = "id", required = true) String id) {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return null;
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        final List<org.jeecg.modules.uav.vo.UavPath.Point> list = new ArrayList<>();
        final CsvReader reader = CsvUtil.getReader();
        final CsvData data = reader.read(FileUtil.file(filePath));
        final List<CsvRow> rows = data.getRows();
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

        final ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, uavPath.getName());
        mv.addObject(NormalExcelConstants.CLASS, org.jeecg.modules.uav.vo.UavPath.Point.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("无人机轨迹数据", uavPath.getName()));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    @RequestMapping(value = "/exportPointCloud")
    public void exportPointCloud(@RequestParam(name = "id", required = true) String id, final HttpServletResponse response) throws IOException {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return;
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        final String fileName = "无人机轨迹数据_" + uavPath.getName() + ".xyz";
        response.setHeader("Content-type", "text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + "\";charset=UTF-8");

        final CsvReader reader = CsvUtil.getReader();
        final CsvData data = reader.read(FileUtil.file(filePath));
        final List<CsvRow> rows = data.getRows();
        for (CsvRow csvRow : rows) {
            List<String> cols = csvRow.getRawList();
            if (cols.get(0).equals("$GPGGA")) {
                // GPS定位信息
                double x = getDistance(Convert.toFloat(cols.get(4)) / 100.0, 0.0, uavPath.getCenterLongitude() / 100.0, 0.0);
                double y = getDistance(0, Convert.toFloat(cols.get(2)) / 100.0, 0.0, uavPath.getCenterLatitude() / 100.0);
                response.getWriter().write(x + "," + y + "," + cols.get(9) + "\r\n");
            }
        }

        response.getWriter().flush();
        response.getWriter().close();
    }
}
