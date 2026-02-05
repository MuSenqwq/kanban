package com.ruoyi.kanban.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.domain.KbList;
import com.ruoyi.kanban.service.IKbBoardService;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.kanban.service.IKbListService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 看板可视化视图控制器
 */
@Controller
@RequestMapping("/kanban/view")
public class KanbanBoardController extends BaseController {

    @Autowired
    private IKbListService listService;

    @Autowired
    private IKbCardService cardService;

    @Autowired
    private IKbBoardService boardService;

    /**
     * 打开看板可视化页面
     */

    @GetMapping("/{boardId}")
    public String index(@PathVariable("boardId") Long boardId, ModelMap mmap) {
        // 1. 查询看板详情
        KbBoard board = boardService.selectKbBoardByBoardId(boardId);

        // 2. 查询所有列
        KbList listQuery = new KbList();
        listQuery.setBoardId(boardId);
        List<KbList> lists = listService.selectKbListList(listQuery);

        // 3. 查询所有卡片
        KbCard cardQuery = new KbCard();
        cardQuery.setBoardId(boardId);
        List<KbCard> allCards = cardService.selectKbCardList(cardQuery);

        // 4. 分配卡片到列
        for (KbList list : lists) {
            List<KbCard> matchedCards = allCards.stream()
                    .filter(c -> c.getListId() != null && c.getListId().equals(list.getListId()))
                    .sorted(Comparator.comparing(KbCard::getOrderNum, Comparator.nullsFirst(Long::compareTo)))
                    .collect(Collectors.toList());
            list.setCards(matchedCards);
        }
        lists.sort(Comparator.comparing(KbList::getOrderNum, Comparator.nullsFirst(Long::compareTo)));

        mmap.put("lists", lists);
        mmap.put("board", board);
        mmap.put("boardId", boardId);
        return "kanban/board/view";
    }

    /**
     * [新增] 获取看板统计数据接口 (用于ECharts)
     */
    @GetMapping("/stats/{boardId}")
    @ResponseBody
    public AjaxResult getBoardStats(@PathVariable("boardId") Long boardId) {
        KbList listQuery = new KbList();
        listQuery.setBoardId(boardId);
        List<KbList> lists = listService.selectKbListList(listQuery);

        KbCard cardQuery = new KbCard();
        cardQuery.setBoardId(boardId);
        List<KbCard> cards = cardService.selectKbCardList(cardQuery);

        // 统计1：各列任务数量
        List<String> listNames = lists.stream().map(KbList::getListName).collect(Collectors.toList());
        List<Integer> listCounts = lists.stream().map(list -> {
            return (int) cards.stream().filter(c -> c.getListId().equals(list.getListId())).count();
        }).collect(Collectors.toList());

        // 统计2：优先级分布 (0=紧急, 1=高, 2=中, 3=低)
        Map<String, Long> priorityStats = cards.stream()
                .collect(Collectors.groupingBy(c -> c.getPriority() == null ? "3" : c.getPriority(), Collectors.counting()));

        // 统计3：总进度
        long totalCount = cards.size();
        long doneCount = cards.stream().filter(c -> "1".equals(c.getStatus())).count(); // 假设1是已完成

        Map<String, Object> data = new HashMap<>();
        data.put("listNames", listNames);
        data.put("listCounts", listCounts);
        data.put("priorityStats", priorityStats);
        data.put("total", totalCount);
        data.put("done", doneCount);

        return AjaxResult.success(data);
    }
}