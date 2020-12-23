package org.jeecg.modules.uav.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.uav.entity.UavTask;
import org.jeecg.modules.uav.entity.UavTaskItem;
import org.jeecg.modules.uav.service.IUavTaskItemService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 测试任务项
 * @Author: jeecg-boot
 * @Date: 2020-12-22
 * @Version: V1.0
 */
@Api(tags = "测试任务项")
@RestController
@RequestMapping("/uav/uavTaskItem")
@Slf4j
public class UavTaskItemController extends JeecgController<UavTaskItem, IUavTaskItemService> {
    @Autowired
    private IUavTaskItemService uavTaskItemService;

    /**
     * 分页列表查询
     *
     * @param uavTaskItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "测试任务项-分页列表查询")
    @ApiOperation(value = "测试任务项-分页列表查询", notes = "测试任务项-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(UavTaskItem uavTaskItem,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<UavTaskItem> queryWrapper = QueryGenerator.initQueryWrapper(uavTaskItem, req.getParameterMap());
        Page<UavTaskItem> page = new Page<UavTaskItem>(pageNo, pageSize);
        IPage<UavTaskItem> pageList = uavTaskItemService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param uavTaskItem
     * @return
     */
    @AutoLog(value = "测试任务项-添加")
    @ApiOperation(value = "测试任务项-添加", notes = "测试任务项-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody UavTaskItem uavTaskItem) {
        uavTaskItemService.save(uavTaskItem);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param uavTaskItem
     * @return
     */
    @AutoLog(value = "测试任务项-编辑")
    @ApiOperation(value = "测试任务项-编辑", notes = "测试任务项-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody UavTaskItem uavTaskItem) {
        uavTaskItemService.updateById(uavTaskItem);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试任务项-通过id删除")
    @ApiOperation(value = "测试任务项-通过id删除", notes = "测试任务项-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        uavTaskItemService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "测试任务项-批量删除")
    @ApiOperation(value = "测试任务项-批量删除", notes = "测试任务项-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.uavTaskItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试任务项-通过id查询")
    @ApiOperation(value = "测试任务项-通过id查询", notes = "测试任务项-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        UavTaskItem uavTaskItem = uavTaskItemService.getById(id);
        if (uavTaskItem == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(uavTaskItem);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param uavTaskItem
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UavTaskItem uavTaskItem) {
        return super.exportXls(request, uavTaskItem, UavTaskItem.class, "测试任务项");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, UavTaskItem.class);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试任务-通过id查询")
    @ApiOperation(value = "测试任务-通过id查询", notes = "测试任务-通过id查询")
    @GetMapping(value = "/queryByTypeAndTask")
    public Result<?> queryByTypeAndTask(@RequestParam(name = "taskId", required = true) String id, @RequestParam(name = "type", required = true) String type) {
        UavTaskItem uavTask = uavTaskItemService.getOne(new QueryWrapper<UavTaskItem>().eq("task_id", id).eq("type", type));
        if (uavTask == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(uavTask);
    }
}
