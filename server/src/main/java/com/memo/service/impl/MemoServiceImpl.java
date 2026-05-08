package com.memo.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memo.common.Result;
import com.memo.dto.MemoDTO;
import com.memo.entity.Memo;
import com.memo.entity.MemoGroup;
import com.memo.mapper.MemoGroupMapper;
import com.memo.mapper.MemoMapper;
import com.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 备忘录服务实现类
 */
@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {

    private final MemoMapper memoMapper;
    private final MemoGroupMapper memoGroupMapper;

    @Override
    public Result list(int page, int pageSize, Long groupId) {
        Long userId = StpUtil.getLoginIdAsLong();

        Page<Memo> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId)
               .eq(Memo::getIsDeleted, 0);

        if (groupId != null) {
            wrapper.eq(Memo::getGroupId, groupId);
        }

        wrapper.orderByDesc(Memo::getUpdatedAt);

        Page<Memo> result = memoMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result detail(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Memo memo = memoMapper.selectById(id);
        if (memo == null) {
            return Result.error(500, "备忘录不存在");
        }
        if (!memo.getUserId().equals(userId)) {
            return Result.error(500, "无权访问该备忘录");
        }
        return Result.success(memo);
    }

    @Override
    public Result create(MemoDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 获取用户的默认分组
        LambdaQueryWrapper<MemoGroup> groupWrapper = new LambdaQueryWrapper<>();
        groupWrapper.eq(MemoGroup::getUserId, userId)
                    .eq(MemoGroup::getIsDefault, 1);
        MemoGroup defaultGroup = memoGroupMapper.selectOne(groupWrapper);

        Long groupId = dto.getGroupId();
        if (groupId == null && defaultGroup != null) {
            groupId = defaultGroup.getId();
        }

        Memo memo = new Memo();
        memo.setUserId(userId);
        memo.setGroupId(groupId);
        memo.setTitle(dto.getTitle());
        memo.setContent(dto.getContent());
        memo.setIsDeleted(0);
        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        memoMapper.insert(memo);

        return Result.success(memo);
    }

    @Override
    public Result update(Long id, MemoDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        Memo memo = memoMapper.selectById(id);
        if (memo == null) {
            return Result.error(500, "备忘录不存在");
        }
        if (!memo.getUserId().equals(userId)) {
            return Result.error(500, "无权修改该备忘录");
        }

        memo.setTitle(dto.getTitle());
        memo.setContent(dto.getContent());
        if (dto.getGroupId() != null) {
            memo.setGroupId(dto.getGroupId());
        }
        memo.setUpdatedAt(LocalDateTime.now());
        memoMapper.updateById(memo);

        return Result.success(memo);
    }

    @Override
    public Result delete(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Memo memo = memoMapper.selectById(id);
        if (memo == null) {
            return Result.error(500, "备忘录不存在");
        }
        if (!memo.getUserId().equals(userId)) {
            return Result.error(500, "无权删除该备忘录");
        }

        memo.setIsDeleted(1);
        memo.setDeletedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        memoMapper.updateById(memo);

        return Result.success("删除成功");
    }

    @Override
    public Result trashList(int page, int pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();

        Page<Memo> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId)
               .eq(Memo::getIsDeleted, 1)
               .orderByDesc(Memo::getUpdatedAt);

        Page<Memo> result = memoMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result restore(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Memo memo = memoMapper.selectById(id);
        if (memo == null) {
            return Result.error(500, "备忘录不存在");
        }
        if (!memo.getUserId().equals(userId)) {
            return Result.error(500, "无权操作该备忘录");
        }

        memo.setIsDeleted(0);
        memo.setDeletedAt(null);
        memo.setUpdatedAt(LocalDateTime.now());
        memoMapper.updateById(memo);

        return Result.success("恢复成功");
    }

    @Override
    public Result forceDelete(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Memo memo = memoMapper.selectById(id);
        if (memo == null) {
            return Result.error(500, "备忘录不存在");
        }
        if (!memo.getUserId().equals(userId)) {
            return Result.error(500, "无权删除该备忘录");
        }

        memoMapper.deleteById(id);
        return Result.success("永久删除成功");
    }
}
