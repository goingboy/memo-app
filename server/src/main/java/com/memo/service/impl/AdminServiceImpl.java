package com.memo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memo.common.Result;
import com.memo.entity.SysUser;
import com.memo.mapper.SysUserMapper;
import com.memo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 管理员服务实现类
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final SysUserMapper sysUserMapper;

    @Override
    public Result userList(int page, int pageSize, String keyword) {
        Page<SysUser> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getEmail, keyword)
                             .or()
                             .like(SysUser::getNickname, keyword));
        }

        wrapper.orderByDesc(SysUser::getCreatedAt);

        Page<SysUser> result = sysUserMapper.selectPage(pageParam, wrapper);
        // 不返回密码
        result.getRecords().forEach(user -> user.setPassword(null));
        return Result.success(result);
    }

    @Override
    public Result updateUserStatus(Long id, Integer status) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            return Result.error(500, "用户不存在");
        }

        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        sysUserMapper.updateById(user);

        return Result.success("更新成功");
    }
}
