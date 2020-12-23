package org.jeecg.modules.uav.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.uav.entity.UavPath;
import org.jeecg.modules.uav.miscs.LasUtils;
import org.jeecg.modules.uav.miscs.NMEAUtils;
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
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 数据可视化控制器
 *
 * @author Alex
 */
@RestController
@RequestMapping("/uav/datav")
@Slf4j
public final class DataVisualization {
    @Autowired
    private IUavPathService uavPathService;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @GetMapping(value = "/paths")
    public final Result<?> getPaths() {
        return Result.ok(uavPathService.list());
    }

    @GetMapping(value = "/path")
    public final Result<?> getPath(@RequestParam(name = "id", required = true) String id) {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return Result.error("未找到对应数据");
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return Result.error("未找到对应数据");
        }

        final List<org.jeecg.modules.uav.vo.UavPath.Point> list = NMEAUtils.getPoints(filePath);
        final double centerLongitude = (uavPath.getCenterLongitude() != null) ? uavPath.getCenterLongitude() : (list != null) && (list.size() > 0) ? list.get(0).getLng() : 0;
        final double centerLatitude = uavPath.getCenterLatitude() != null ? uavPath.getCenterLatitude() : (list != null) && (list.size() > 0) ? list.get(0).getLat() : 0;
        final double centerAltitude = uavPath.getCenterAltitude() != null ? uavPath.getCenterAltitude() : (list != null) && (list.size() > 0) ? list.get(0).getAlt() : 0;

        final org.jeecg.modules.uav.vo.UavPath path = new org.jeecg.modules.uav.vo.UavPath();
        path.setId(uavPath.getId());
        path.setName(uavPath.getName());
        path.setDescription(uavPath.getDescription());
        path.setCenterLongitude(centerLongitude);
        path.setCenterLatitude(centerLatitude);
        path.setCenterAltitude(centerAltitude);
        path.setPoints(list);

        return Result.ok(path);
    }

    @RequestMapping(value = "/exportXls")
    public final ModelAndView exportXls(@RequestParam(name = "id", required = true) String id) {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return null;
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        final ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, uavPath.getName());
        mv.addObject(NormalExcelConstants.CLASS, org.jeecg.modules.uav.vo.UavPath.Point.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("无人机轨迹数据", uavPath.getName()));
        mv.addObject(NormalExcelConstants.DATA_LIST, NMEAUtils.getPoints(filePath));

        return mv;
    }

    @RequestMapping(value = "/exportPointCloud")
    public final void exportPointCloud(@RequestParam(name = "id", required = true) String id, final HttpServletResponse response) throws Exception {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return;
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        final String fileName = "无人机轨迹数据_" + uavPath.getName() + ".las";
        response.setHeader("Content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + "\";charset=UTF-8");

        try (final BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            final List<org.jeecg.modules.uav.vo.UavPath.OffsetPoint> list = NMEAUtils.getOffsetPoints(filePath, uavPath.getCenterLongitude(), uavPath.getCenterLatitude(), uavPath.getCenterAltitude());
            if (list != null) {
                LasUtils.generateLasFile(bos, list);
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }

        response.flushBuffer();
    }

    @RequestMapping(value = "/exportPointCloudWithFile")
    public final void exportPointCloudWithFile(@RequestParam(name = "filePath", required = true) String fileName, final HttpServletResponse response) throws Exception {
        final String filePath = uploadpath + File.separator + fileName;
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        final String name = "无人机轨迹数据_" + fileName + ".las";
        response.setHeader("Content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(name, StandardCharsets.UTF_8.name()) + "\";charset=UTF-8");

        try (final BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            final List<org.jeecg.modules.uav.vo.UavPath.OffsetPoint> list = NMEAUtils.getOffsetPoints(filePath, null, null, null);
            if (list != null) {
                LasUtils.generateLasFile(bos, list);
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }

        response.flushBuffer();
    }
}
