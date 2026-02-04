package com.ruoyi.kanban.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.service.IKbBoardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 任务看板Controller
 * * @author ruoyi
 * @date 2026-02-03
 */
@Controller
@RequestMapping("/kanban/board") // [修改点] 统一去掉 ruoyi- 前缀，保持简洁

public class KbBoardController extends BaseController
{
    private String prefix = "kanban/board"; // [修改点] 对应 templates/kanban/board 目录

    @Autowired
    private IKbBoardService kbBoardService;

    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    @GetMapping()
    public String board()
    {
        return prefix + "/board";
    }

    /**
     * 查询任务看板列表
     */

    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KbBoard kbBoard)
    {
        startPage();
        List<KbBoard> list = kbBoardService.selectKbBoardList(kbBoard);
        return getDataTable(list);
    }

    /**
     * 导出任务看板列表
     */
    @RequiresPermissions("kanban:board:export")
    @Log(title = "任务看板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KbBoard kbBoard)
    {
        List<KbBoard> list = kbBoardService.selectKbBoardList(kbBoard);
        ExcelUtil<KbBoard> util = new ExcelUtil<KbBoard>(KbBoard.class);
        return util.exportExcel(list, "任务看板数据");
    }

    /**
     * 新增任务看板
     */
    @RequiresPermissions("kanban:board:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存任务看板
     */
    @RequiresPermissions("kanban:board:add")
    @Log(title = "任务看板", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KbBoard kbBoard)
    {
        return toAjax(kbBoardService.insertKbBoard(kbBoard));
    }

    /**
     * 修改任务看板
     */
    @RequiresPermissions("kanban:board:edit")
    @GetMapping("/edit/{boardId}")
    public String edit(@PathVariable("boardId") Long boardId, ModelMap mmap)
    {
        KbBoard kbBoard = kbBoardService.selectKbBoardByBoardId(boardId);
        mmap.put("kbBoard", kbBoard);
        return prefix + "/edit";
    }

    /**
     * 修改保存任务看板
     */
    @RequiresPermissions("kanban:board:edit")
    @Log(title = "任务看板", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KbBoard kbBoard)
    {
        return toAjax(kbBoardService.updateKbBoard(kbBoard));
    }

    /**
     * 删除任务看板
     */
    @RequiresPermissions("kanban:board:remove")
    @Log(title = "任务看板", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kbBoardService.deleteKbBoardByBoardIds(ids));
    }
}