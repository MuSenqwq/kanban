package com.ruoyi.kanban.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.mapper.KbCardMapper;

@Controller
@RequestMapping("/kanban/main")
public class KbMainController extends BaseController {

    @Autowired
    private KbCardMapper kbCardMapper;

    @GetMapping("/stats")
    @ResponseBody
    public AjaxResult stats() {
        Long userId = ShiroUtils.getUserId();

        // 1. 获取统计 Map
        Map<String, Object> stats = kbCardMapper.selectUserTaskStats(userId);
        if (stats == null) {
            stats = new HashMap<>();
        }

        // 2. 补全未读消息
        int unreadMsg = kbCardMapper.countUnreadMessages(userId);
        stats.put("unreadMsg", unreadMsg);

        // 3. 即将到期列表
        List<KbCard> upcomingList = kbCardMapper.selectUpcomingTasks(userId);
        stats.put("upcomingList", upcomingList);

        return AjaxResult.success(stats);
    }
}