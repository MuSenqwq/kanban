package com.ruoyi.kanban.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.ruoyi.kanban.domain.KbTeam;
import com.ruoyi.kanban.service.IKbTeamService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.core.domain.entity.SysUser; // 修改了这里
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 团队信息Controller
 * * @author ruoyi
 * @date 2026-02-06
 */
@Controller
@RequestMapping("/ruoyi-kanban/team")
public class KbTeamController extends BaseController
{
    private String prefix = "kanban/team";

    @Autowired
    private IKbTeamService kbTeamService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("ruoyi-kanban:team:view")
    @GetMapping()
    public String team()
    {
        return prefix + "/team";
    }

    /**
     * 查询团队信息列表
     */
    @RequiresPermissions("ruoyi-kanban:team:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KbTeam kbTeam)
    {
        startPage();
        List<KbTeam> list = kbTeamService.selectKbTeamList(kbTeam);
        return getDataTable(list);
    }

    /**
     * 导出团队信息列表
     */
    @RequiresPermissions("ruoyi-kanban:team:export")
    @Log(title = "团队信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KbTeam kbTeam)
    {
        List<KbTeam> list = kbTeamService.selectKbTeamList(kbTeam);
        ExcelUtil<KbTeam> util = new ExcelUtil<KbTeam>(KbTeam.class);
        return util.exportExcel(list, "团队信息数据");
    }

    /**
     * 新增团队信息
     */
    @RequiresPermissions("ruoyi-kanban:team:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        // 获取所有用户，用于前端下拉框选择
        mmap.put("users", userService.selectUserList(new SysUser()));
        return prefix + "/add";
    }

    /**
     * 新增保存团队信息
     */
    @RequiresPermissions("ruoyi-kanban:team:add")
    @Log(title = "团队信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KbTeam kbTeam)
    {
        return toAjax(kbTeamService.insertKbTeam(kbTeam));
    }

    /**
     * 修改团队信息
     */
    @RequiresPermissions("ruoyi-kanban:team:edit")
    @GetMapping("/edit/{teamId}")
    public String edit(@PathVariable("teamId") Long teamId, ModelMap mmap)
    {
        KbTeam kbTeam = kbTeamService.selectKbTeamByTeamId(teamId);
        mmap.put("kbTeam", kbTeam);
        // 获取所有用户，用于前端下拉框选择
        mmap.put("users", userService.selectUserList(new SysUser()));
        // 获取该团队已选中的成员ID列表，用于回显
        mmap.put("selectedUserIds", kbTeamService.selectUserIdsByTeamId(teamId));
        return prefix + "/edit";
    }

    /**
     * 修改保存团队信息
     */
    @RequiresPermissions("ruoyi-kanban:team:edit")
    @Log(title = "团队信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KbTeam kbTeam)
    {
        return toAjax(kbTeamService.updateKbTeam(kbTeam));
    }

    /**
     * 删除团队信息
     */
    @RequiresPermissions("ruoyi-kanban:team:remove")
    @Log(title = "团队信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kbTeamService.deleteKbTeamByTeamIds(ids));
    }
}