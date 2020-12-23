package org.jeecg.modules.uav.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.uav.entity.UavPath;
import org.jeecg.modules.uav.service.IUavPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 无人机轨迹数据
 * @Author: jeecg-boot
 * @Date: 2020-12-18
 * @Version: V1.0
 */
@Api(tags = "无人机轨迹数据")
@RestController
@RequestMapping("/uav/uavPath")
@Slf4j
public class UavPathController extends JeecgController<UavPath, IUavPathService> {
    @Autowired
    private IUavPathService uavPathService;

    /**
     * 分页列表查询
     *
     * @param uavPath
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "无人机轨迹数据-分页列表查询")
    @ApiOperation(value = "无人机轨迹数据-分页列表查询", notes = "无人机轨迹数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(UavPath uavPath,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<UavPath> queryWrapper = QueryGenerator.initQueryWrapper(uavPath, req.getParameterMap());
        Page<UavPath> page = new Page<UavPath>(pageNo, pageSize);
        IPage<UavPath> pageList = uavPathService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param uavPath
     * @return
     */
    @AutoLog(value = "无人机轨迹数据-添加")
    @ApiOperation(value = "无人机轨迹数据-添加", notes = "无人机轨迹数据-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody UavPath uavPath) {
        uavPathService.save(uavPath);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param uavPath
     * @return
     */
    @AutoLog(value = "无人机轨迹数据-编辑")
    @ApiOperation(value = "无人机轨迹数据-编辑", notes = "无人机轨迹数据-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody UavPath uavPath) {
        uavPathService.updateById(uavPath);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "无人机轨迹数据-通过id删除")
    @ApiOperation(value = "无人机轨迹数据-通过id删除", notes = "无人机轨迹数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        uavPathService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "无人机轨迹数据-批量删除")
    @ApiOperation(value = "无人机轨迹数据-批量删除", notes = "无人机轨迹数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.uavPathService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "无人机轨迹数据-通过id查询")
    @ApiOperation(value = "无人机轨迹数据-通过id查询", notes = "无人机轨迹数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(uavPath);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param uavPath
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UavPath uavPath) {
        return super.exportXls(request, uavPath, UavPath.class, "无人机轨迹数据");
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
        return super.importExcel(request, response, UavPath.class);
    }

}
