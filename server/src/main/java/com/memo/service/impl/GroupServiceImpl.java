package com.memo.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.memo.common.Result;
import com.memo.dto.GroupDTO;
import com.memo.entity.Memo;
import com.memo.entity.MemoGroup;
import com.memo.mapper.MemoGroupMapper;
import com.memo.mapper.MemoMapper;
import com.memo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分组服务实现类
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final MemoGroupMapper memoGroupMapper;
    private final MemoMapper memoMapper;

    @Override
    public Result list() {
        Long userId = StpUtil.getLoginIdAsLong();

        LambdaQueryWrapper<MemoGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemoGroup::getUserId, userId)
               .orderByAsc(MemoGroup::getSortOrder);

        List<MemoGroup> groups = memoGroupMapper.selectList(wrapper);
        return Result.success(groups);
    }

    @Override
    public Result create(GroupDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();

        MemoGroup group = new MemoGroup();
        group.setUserId(userId);
        group.setName(dto.getName());
        group.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        group.setIsDefault(0);
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedAt(LocalDateTime.now());
        memoGroupMapper.insert(group);

        return Result.success(group);
    }

    @Override
    public Result update(Long id, GroupDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        MemoGroup group = memoGroupMapper.selectById(id);
        if (group == null) {
            return Result.error(500, "分组不存在");
        }
        if (!group.getUserId().equals(userId)) {
            return Result.error(500, "无权修改该分组");
        }

        if (dto.getName() != null) {
            group.setName(dto.getName());
        }
        if (dto.getSortOrder() != null) {
            group.setSortOrder(dto.getSortOrder());
        }
        group.setUpdatedAt(LocalDateTime.now());
        memoGroupMapper.updateById(group);

        return Result.success(group);
    }

    @Override
    public Result delete(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        MemoGroup group = memoGroupMapper.selectById(id);
        if (group == null) {
            return Result.error(500, "分组不存在");
        }
        if (!group.getUserId().equals(userId)) {
            return Result.error(500, "无权删除该分组");
        }

        // 默认分组不允许删除
        if (group.getIsDefault() == 1) {
            return Result.error(500, "默认分组不允许删除");
        }

        // 查找用户的默认分组
        LambdaQueryWrapper<MemoGroup> defaultWrapper = new LambdaQueryWrapper<>();
        defaultWrapper.eq(MemoGroup::getUserId, userId)
                      .eq(MemoGroup::getIsDefault, 1);
        MemoGroup defaultGroup = memoGroupMapper.selectOne(defaultWrapper);
        if (defaultGroup == null) {
            return Result.error(500, "默认分组不存在");
        }

        // 将该分组下未删除的备忘录移到默认分组
        LambdaUpdateWrapper<Memo> memoUpdateWrapper = new LambdaUpdateWrapper<>();
        memoUpdateWrapper.eq(Memo::getUserId, userId)
                         .eq(Memo::getGroupId, id)
                         .eq(Memo::getIsDeleted, 0)
                         .set(Memo::getGroupId, defaultGroup.getId())
                         .set(Memo::getUpdatedAt, LocalDateTime.now());
        memoMapper.update(null, memoUpdateWrapper);

        // 删除分组
        memoGroupMapper.deleteById(id);

        return Result.success("删除成功");
    }
}
