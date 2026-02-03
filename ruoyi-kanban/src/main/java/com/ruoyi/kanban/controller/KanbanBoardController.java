package com.ruoyi.kanban.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.domain.KbList;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.kanban.service.IKbListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
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

    /**
     * 打开看板可视化页面
     * 访问路径示例: /kanban/view/100 (100为看板ID)
     */
    @GetMapping("/{boardId}")
    public String index(@PathVariable("boardId") Long boardId, ModelMap mmap) {
        // 1. 查询该看板下的所有列
        KbList listQuery = new KbList();
        listQuery.setBoardId(boardId);
        List<KbList> lists = listService.selectKbListList(listQuery);

        // 2. 查询该看板下的所有卡片
        KbCard cardQuery = new KbCard();
        cardQuery.setBoardId(boardId);
        List<KbCard> allCards = cardService.selectKbCardList(cardQuery);

        // 3. 核心逻辑：在内存中将卡片分配到对应的列中
        for (KbList list : lists) {
            List<KbCard> matchedCards = allCards.stream()
                    // 筛选归属于当前列的卡片
                    .filter(c -> c.getListId() != null && c.getListId().equals(list.getListId()))
                    // 按 orderNum 排序，防止空指针
                    .sorted(Comparator.comparing(KbCard::getOrderNum, Comparator.nullsFirst(Long::compareTo)))
                    .collect(Collectors.toList());

            // 设置到刚才在 KbList 增加的 cards 字段中
            list.setCards(matchedCards);
        }

        // 4. 对列本身也按 orderNum 排序
        lists.sort(Comparator.comparing(KbList::getOrderNum, Comparator.nullsFirst(Long::compareTo)));

        mmap.put("lists", lists);
        mmap.put("boardId", boardId);
        return "kanban/board/view";
    }
}