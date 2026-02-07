package com.ruoyi.kanban.controller;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.kanban.domain.KbBoardMember;
import com.ruoyi.kanban.service.IKbBoardMemberService;
import com.ruoyi.system.service.ISysUserService;
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
    private IKbBoardMemberService kbBoardMemberService; // [新增] 成员服务

    @Autowired
    private ISysUserService userService; // [新增] 用户服务


    @Autowired
    private IKbBoardService kbBoardService;

    @GetMapping()
    public String board()
    {
        return prefix + "/board";
    }


    /**
     * 导出任务看板列表
     */
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
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存任务看板
     */
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
    @Log(title = "任务看板", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KbBoard kbBoard)
    {
        System.out.println(2);
        if(kbBoard!=null){
            System.out.println(3);
        }else {
            System.out.println(1);
        }
        return toAjax(kbBoardService.updateKbBoard(kbBoard));
    }

    /**
     * 删除任务看板
     */
    @Log(title = "任务看板", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kbBoardService.deleteKbBoardByBoardIds(ids));
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KbBoard kbBoard)
    {
        startPage();
        List<KbBoard> list = kbBoardService.selectKbBoardList(kbBoard);
        return getDataTable(list);
    }

    // [新增方法] 跳转到邀请成员页面
    @GetMapping("/invite/{boardId}")
    public String invite(@PathVariable("boardId") Long boardId, ModelMap mmap)
    {
        mmap.put("boardId", boardId);
        // 获取所有用户供选择 (实际场景可优化为搜索或过滤)
        mmap.put("users", userService.selectUserList(new SysUser()));
        return prefix + "/invite";
    }

    // [新增方法] 保存成员
    @Log(title = "看板成员", businessType = BusinessType.INSERT)
    @PostMapping("/addMember")
    @ResponseBody
    public AjaxResult addMemberSave(KbBoardMember kbBoardMember)
    {
        return toAjax(kbBoardMemberService.insertKbBoardMember(kbBoardMember));
    }

    // [新增方法] 获取当前看板成员列表(用于管理界面)
    @PostMapping("/listMembers")
    @ResponseBody
    public TableDataInfo listMembers(KbBoardMember kbBoardMember)
    {
        startPage();
        List<KbBoardMember> list = kbBoardMemberService.selectKbBoardMemberList(kbBoardMember);
        return getDataTable(list);
    }

}