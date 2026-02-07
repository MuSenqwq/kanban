package com.ruoyi.kanban.controller;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.kanban.domain.AssignUser;
import com.ruoyi.kanban.service.IKbCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysUserMessage;
import com.ruoyi.system.service.ISysUserMessageService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.transaction.annotation.Transactional;
/**
 * 我的消息Controller
 */
@Controller
@RequestMapping("/system/message")
public class SysUserMessageController extends BaseController
{
    @Autowired
    private IKbCardService kbCardService;
    private String prefix = "system/message";

    @Autowired
    private ISysUserMessageService sysUserMessageService;

    // 1. 跳转到消息列表页
    @GetMapping()
    public String message(ModelMap model)
    {
        List<AssignUser> users = kbCardService.selectAllAssignUser();
        model.put("user",users);
        return prefix + "/message";
    }

    // 2. 查询我的消息列表
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserMessage sysUserMessage){
        startPage();
        sysUserMessage.setUserId(ShiroUtils.getUserId());
        List<SysUserMessage> list = sysUserMessageService.selectSysUserMessageList(sysUserMessage);
        return getDataTable(list);
    }

    // 3. 标记消息为已读
    @PostMapping("/read")
    @ResponseBody
    public AjaxResult read(Long messageId)
    {
        SysUserMessage msg = new SysUserMessage();
        msg.setMessageId(messageId);
        msg.setIsRead("1"); // 1=已读
        return toAjax(sysUserMessageService.updateSysUserMessage(msg));
    }

    // 4. 全部已读
    @PostMapping("/readAll")
    @ResponseBody
    public AjaxResult readAll()
    {
        // 简单实现：查询所有未读并循环更新（生产环境建议在Mapper写批量更新SQL）
        SysUserMessage query = new SysUserMessage();
        query.setUserId(ShiroUtils.getUserId());
        query.setIsRead("0");
        List<SysUserMessage> list = sysUserMessageService.selectSysUserMessageList(query);
        for (SysUserMessage msg : list) {
            msg.setIsRead("1");
            sysUserMessageService.updateSysUserMessage(msg);
        }
        return success();
    }

    // 5. 删除消息
    @Log(title = "我的消息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        // 这里为了简单暂未实现批量删除Service，您可以自行补充
        return error("暂不支持删除，请使用'已读'功能");
    }
    /**
     * 发布系统通知（核心接口）
     */
    @Log(title = "发布系统通知", businessType = BusinessType.INSERT)
    @PostMapping("/publish")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult publishMessage(@RequestParam Map<String, Object> params) {
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        String type = (String) params.get("type");
        String scope = (String) params.get("scope");
        String receiveUserIds = (String) params.get("receiveUserIds");

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content)) {
            return AjaxResult.error("通知标题和内容不能为空！");
        }
        if (StringUtils.isEmpty(type)) {
            return AjaxResult.error("请选择消息类型！");
        }
        if (StringUtils.isEmpty(scope)) {
            return AjaxResult.error("请选择接收范围！");
        }
        List<SysUserMessage> messageList = new ArrayList<>();
        if ("all".equals(scope)) {
            // 发给所有用户
            List<AssignUser> allUsers = kbCardService.selectAllAssignUser();
            for (AssignUser user : allUsers) {
                SysUserMessage message = new SysUserMessage();
                message.setUserId(user.getUserId());
                message.setTitle(title);
                message.setContent(content);
                message.setType(type);
                message.setIsRead("0");
                message.setCreateTime(new Date());
                messageList.add(message);
            }
        } else if ("specified".equals(scope)) {
            // 发给指定用户
            if (StringUtils.isEmpty(receiveUserIds)) {
                return AjaxResult.error("请选择接收通知的用户！");
            }
            List<Long> userIdList = Arrays.stream(receiveUserIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            for (Long userId : userIdList) {
                SysUserMessage message = new SysUserMessage();
                message.setUserId(userId);
                message.setTitle(title);
                message.setContent(content);
                message.setType(type);
                message.setIsRead("0");
                message.setCreateTime(new Date());
                messageList.add(message);
            }
        }
        if (!messageList.isEmpty()) {
            sysUserMessageService.batchInsert(messageList);
        }
        return AjaxResult.success("通知发布成功！共发送" + messageList.size() + "条消息");
    }
}