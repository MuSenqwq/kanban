package com.ruoyi.kanban.controller;

import java.util.List;

import com.ruoyi.kanban.domain.AssignUser;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.domain.KbList;
import com.ruoyi.kanban.service.IKbBoardService;
import com.ruoyi.kanban.service.IKbListService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;



/**
 * 任务卡片Controller
 */
@Controller
@RequestMapping("/kanban/card")
public class KbCardController extends BaseController
{
    private String prefix = "kanban/card";

    @Autowired
    private IKbCardService kbCardService;

    @Autowired
    private IKbBoardService kbBoardService; // [新增]

    @Autowired
    private IKbListService kbListService;   // [新增]


    // --- 标准CRUD接口 ---


    @GetMapping()
    public String card()
    {
        return prefix + "/card";
    }

    @RequiresPermissions("kanban:card:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KbCard kbCard)
    {
        startPage();
        kbCard.setExecutorId(ShiroUtils.getUserId());
        List<KbCard> list = kbCardService.selectKbCardList(kbCard);
        return getDataTable(list);
    }


    @Log(title = "任务卡片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KbCard kbCard)
    {
        List<KbCard> list = kbCardService.selectKbCardList(kbCard);
        ExcelUtil<KbCard> util = new ExcelUtil<KbCard>(KbCard.class);
        return util.exportExcel(list, "card");
    }

    /**
     * 新增任务卡片 (修改版：支持从任务池进入)
     */
    @GetMapping("/add")
    public String add(Long listId, Long boardId, ModelMap mmap)
    {
        // 如果是从任务池点的新增，没有 boardId，需要加载所有可选看板供用户选择
        if (boardId == null) {
            KbBoard query = new KbBoard();
            // 这里简单查询所有，实际项目可根据 userId 过滤: query.setUserId(ShiroUtils.getUserId());
            List<KbBoard> boards = kbBoardService.selectKbBoardList(query);
            mmap.put("boards", boards);
        }
        mmap.put("listId", listId);
        mmap.put("boardId", boardId);
        return prefix + "/add";
    }

    /**
     * [新增接口] 根据看板ID获取清单列表 (用于下拉框联动)
     */
    @GetMapping("/getLists")
    @ResponseBody
    public AjaxResult getLists(Long boardId) {
        if (boardId == null) return AjaxResult.error();
        KbList query = new KbList();
        query.setBoardId(boardId);
        return AjaxResult.success(kbListService.selectKbListList(query));
    }


    @Log(title = "任务卡片", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KbCard kbCard)
    {
        if (kbCard.getListId() == null || kbCard.getBoardId() == null) {
            return error("归属看板和清单不能为空");
        }
        return toAjax(kbCardService.insertKbCard(kbCard));
    }

    @GetMapping("/edit/{cardId}")
    public String edit(@PathVariable("cardId") Long cardId, ModelMap mmap)
    {
        KbCard kbCard = kbCardService.selectKbCardByCardId(cardId);
        mmap.put("kbCard", kbCard);
        return prefix + "/edit";
    }


    @Log(title = "任务卡片", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KbCard kbCard)
    {
        return toAjax(kbCardService.updateKbCard(kbCard));
    }


    @Log(title = "任务卡片", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kbCardService.deleteKbCardByCardIds(ids));
    }

    /**
     * 拖拽排序/跨列移动（保存目标列顺序 + 来源列顺序）
     */
    @PostMapping("/changeOrder")
    @ResponseBody
    public AjaxResult changeOrder(Long cardId, Long listId, String sortOrder, Long fromListId, String fromSortOrder) {
        return toAjax(kbCardService.changeCardOrder(cardId, listId, sortOrder, fromListId, fromSortOrder));
    }

    // --- 业务接口 ---

    @GetMapping("/my")
    public String myTasks() { return prefix + "/my"; }

    @PostMapping("/my/list")
    @ResponseBody
    public TableDataInfo myList(KbCard kbCard) {
        startPage();
        kbCard.setExecutorId(ShiroUtils.getUserId());
        List<KbCard> list = kbCardService.selectKbCardList(kbCard);
        return getDataTable(list);
    }

    @GetMapping("/pool")
    public String pool(Model model) {
        Subject subject = SecurityUtils.getSubject();
        boolean isAdmin = subject.hasRole("admin");
        model.addAttribute("isAdmin", isAdmin);
        return prefix + "/pool";
    }
    @PostMapping("/pool/list")
    @ResponseBody
    public TableDataInfo poolList() {
        startPage();
        List<KbCard> list = kbCardService.selectTaskPoolList(ShiroUtils.getUserId());
        return getDataTable(list);
    }

    @PostMapping("/claim")
    @ResponseBody
    public AjaxResult claim(Long cardId) {
        return toAjax(kbCardService.claimTask(cardId, ShiroUtils.getUserId()));
    }

    @PostMapping("/complete")
    @ResponseBody
    public AjaxResult complete(Long cardId) {
        return toAjax(kbCardService.completeTask(cardId));
    }

    // 查询所有可指派的用户列表
    @GetMapping("/assignUser")
    @ResponseBody
    public AjaxResult listUser(@RequestParam(name = "cardId")String cardId) {
        System.out.println("=====请求到达/assignUser，cardId：" + cardId + "====");
        List<AssignUser> users = kbCardService.selectAllAssignUser();
        return AjaxResult.success("查询可指派用户成功", users).put("cardId", cardId);
    }

    @PostMapping("/assign")
    @ResponseBody
    public AjaxResult assign(@RequestParam Long cardId, @RequestParam Long executorId) {
        return toAjax(kbCardService.claimTask(cardId, executorId));
    }
}
