package com.ruoyi.kanban.service.impl;

import java.util.List;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.domain.KbBoardMember;
import com.ruoyi.kanban.mapper.KbBoardMapper;
import com.ruoyi.kanban.mapper.KbBoardMemberMapper;
import com.ruoyi.kanban.service.IKbBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务看板Service业务层处理
 *
 * @author ruoyi
 * @date 2026-02-03
 */
@Service
public class KbBoardServiceImpl implements IKbBoardService
{
    @Autowired
    private KbBoardMapper kbBoardMapper;

    @Autowired
    private KbBoardMemberMapper kbBoardMemberMapper;

    /**
     * 查询任务看板
     *
     * @param boardId 任务看板主键
     * @return 任务看板
     */
    @Override
    public KbBoard selectKbBoardByBoardId(Long boardId)
    {
        return kbBoardMapper.selectKbBoardByBoardId(boardId);
    }

    /**
     * 查询任务看板列表
     *
     * 规则：
     * - 管理员：看全部
     * - 普通用户：看自己加入的看板（通过 kb_board_member 过滤，XML里实现）
     *
     * @param kbBoard 任务看板
     * @return 任务看板
     */
    @Override
    public List<KbBoard> selectKbBoardList(KbBoard kbBoard)
    {
        Long userId = ShiroUtils.getUserId();
        if (!ShiroUtils.isAdmin(userId))
        {
            kbBoard.setUserId(userId);
        }
        return kbBoardMapper.selectKbBoardList(kbBoard);
    }

    /**
     * 新增任务看板
     *
     * 新增看板后：自动把创建者写入 kb_board_member（role=0 管理员）
     *
     * @param kbBoard 任务看板
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertKbBoard(KbBoard kbBoard)
    {
        Long userId = ShiroUtils.getUserId();

        // 设置创建人ID、账号、时间
        kbBoard.setUserId(userId);
        kbBoard.setCreateBy(ShiroUtils.getLoginName());
        kbBoard.setCreateTime(DateUtils.getNowDate());

        int rows = kbBoardMapper.insertKbBoard(kbBoard);

        // 插入成员关系：创建者 = 看板管理员(0)
        if (rows > 0 && kbBoard.getBoardId() != null)
        {
            KbBoardMember member = new KbBoardMember();
            member.setBoardId(kbBoard.getBoardId());
            member.setUserId(userId);
            member.setRole("0");
            member.setCreateTime(DateUtils.getNowDate());
            kbBoardMemberMapper.insertKbBoardMember(member);
        }

        return rows;
    }

    /**
     * 修改任务看板
     *
     * @param kbBoard 任务看板
     * @return 结果
     */
    @Override
    public int updateKbBoard(KbBoard kbBoard)
    {
        // 设置更新人、时间
        kbBoard.setUpdateBy(ShiroUtils.getLoginName());
        kbBoard.setUpdateTime(DateUtils.getNowDate());
        return kbBoardMapper.updateKbBoard(kbBoard);
    }

    /**
     * 批量删除任务看板
     *
     * @param boardIds 需要删除的任务看板主键
     * @return 结果
     */
    @Override
    public int deleteKbBoardByBoardIds(String boardIds)
    {
        return kbBoardMapper.deleteKbBoardByBoardIds(Convert.toStrArray(boardIds));
    }

    /**
     * 删除任务看板信息
     *
     * @param boardId 任务看板主键
     * @return 结果
     */
    @Override
    public int deleteKbBoardByBoardId(Long boardId)
    {
        return kbBoardMapper.deleteKbBoardByBoardId(boardId);
    }
}
