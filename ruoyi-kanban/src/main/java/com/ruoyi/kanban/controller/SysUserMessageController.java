package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.SysUserMessage;
import com.ruoyi.system.service.ISysUserMessageService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;

/**
 * 我的消息Controller
 */
@Controller
@RequestMapping("/system/message")
public class SysUserMessageController extends BaseController
{
    private String prefix = "system/message";

    @Autowired
    private ISysUserMessageService sysUserMessageService;

    // 1. 跳转到消息列表页
    @GetMapping()
    public String message()
    {
        return prefix + "/message";
    }

    // 2. 查询我的消息列表
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserMessage sysUserMessage)
    {
        startPage();
        // 强制只查当前登录用户的消息
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
}