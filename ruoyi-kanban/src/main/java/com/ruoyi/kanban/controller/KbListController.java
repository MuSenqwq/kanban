package com.ruoyi.kanban.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
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
import com.ruoyi.kanban.domain.KbList;
import com.ruoyi.kanban.service.IKbListService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 看板任务列Controller
 *
 * @author ruoyi
 * @date 2026-02-03
 */
@Controller
@RequestMapping({"/ruoyi-kanban/list", "/kanban/list"})
public class KbListController extends BaseController
{
    private String prefix = "ruoyi-kanban/list";

    @Autowired
    private IKbListService kbListService;

    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    @GetMapping()
    public String list()
    {
        return prefix + "/list";
    }

    /**
     * 查询看板任务列列表
     */
    @PostMapping("/list")
    @ResponseBody
    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    public TableDataInfo list(KbList kbList)
    {
        startPage();
        List<KbList> list = kbListService.selectKbListList(kbList);
        return getDataTable(list);
    }

    /**
     * 导出看板任务列列表
     */
    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    @Log(title = "看板任务列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KbList kbList)
    {
        List<KbList> list = kbListService.selectKbListList(kbList);
        ExcelUtil<KbList> util = new ExcelUtil<KbList>(KbList.class);
        return util.exportExcel(list, "看板任务列数据");
    }

    /**
     * 新增看板任务列
     */
    @RequiresRoles("admin")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存看板任务列
     */
    @RequiresRoles("admin")
    @Log(title = "看板任务列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KbList kbList)
    {
        return toAjax(kbListService.insertKbList(kbList));
    }

    /**
     * 修改看板任务列
     */
    @RequiresRoles("admin")
    @GetMapping("/edit/{listId}")
    public String edit(@PathVariable("listId") Long listId, ModelMap mmap)
    {
        KbList kbList = kbListService.selectKbListByListId(listId);
        mmap.put("kbList", kbList);
        return prefix + "/edit";
    }

    /**
     * 修改保存看板任务列
     */
    @RequiresRoles("admin")
    @Log(title = "看板任务列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KbList kbList)
    {
        return toAjax(kbListService.updateKbList(kbList));
    }

    /**
     * 删除看板任务列
     */
    @RequiresRoles("admin")
    @Log(title = "看板任务列", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kbListService.deleteKbListByListIds(ids));
    }
}
