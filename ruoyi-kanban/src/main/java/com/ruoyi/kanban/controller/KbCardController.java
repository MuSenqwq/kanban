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
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 任务卡片Controller
 *
 * @author ruoyi
 * @date 2026-02-03
 */
@Controller
@RequestMapping("/kanban/card")
public class KbCardController extends BaseController
{
    private String prefix = "kanban/card";

    @Autowired
    private IKbCardService kbCardService;

    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    @GetMapping()
    public String card()
    {
        return prefix + "/card";
    }

    /**
     * 查询任务卡片列表
     */
    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KbCard kbCard)
    {
        startPage();
        List<KbCard> list = kbCardService.selectKbCardList(kbCard);
        return getDataTable(list);
    }

    /**
     * 导出任务卡片列表
     */
    @RequiresPermissions("kanban:card:export")
    @Log(title = "任务卡片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KbCard kbCard)
    {
        List<KbCard> list = kbCardService.selectKbCardList(kbCard);
        ExcelUtil<KbCard> util = new ExcelUtil<KbCard>(KbCard.class);
        return util.exportExcel(list, "任务卡片数据");
    }

    /**
     * 新增任务卡片
     */
    @RequiresPermissions("kanban:card:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存任务卡片
     */
    @RequiresPermissions("kanban:card:add")
    @Log(title = "任务卡片", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KbCard kbCard)
    {
        return toAjax(kbCardService.insertKbCard(kbCard));
    }

    /**
     * 修改任务卡片
     */
    @RequiresPermissions("kanban:card:edit")
    @GetMapping("/edit/{cardId}")
    public String edit(@PathVariable("cardId") Long cardId, ModelMap mmap)
    {
        KbCard kbCard = kbCardService.selectKbCardByCardId(cardId);
        mmap.put("kbCard", kbCard);
        return prefix + "/edit";
    }

    /**
     * 修改保存任务卡片
     */
    @RequiresPermissions("kanban:card:edit")
    @Log(title = "任务卡片", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KbCard kbCard)
    {
        return toAjax(kbCardService.updateKbCard(kbCard));
    }

    /**
     * 删除任务卡片
     */
    @RequiresRoles("admin")
    @RequiresPermissions("kanban:card:remove")
    @Log(title = "任务卡片", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kbCardService.deleteKbCardByCardIds(ids));
    }

    @RequiresPermissions("kanban:card:edit")
    @Log(title = "任务卡片", businessType = BusinessType.UPDATE)
    @PostMapping("/changeOrder")
    @ResponseBody
    public AjaxResult changeOrder(Long cardId, Long listId, String sortOrder)
    {
        return toAjax(kbCardService.changeCardOrder(cardId, listId, sortOrder));
    }

}
