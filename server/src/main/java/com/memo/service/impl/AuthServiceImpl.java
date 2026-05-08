package com.memo.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.memo.common.Result;
import com.memo.dto.LoginDTO;
import com.memo.dto.RegisterDTO;
import com.memo.dto.UserProfileDTO;
import com.memo.entity.MemoGroup;
import com.memo.entity.SysUser;
import com.memo.mapper.MemoGroupMapper;
import com.memo.mapper.SysUserMapper;
import com.memo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final MemoGroupMapper memoGroupMapper;

    /** 管理员邮箱 */
    private static final String ADMIN_EMAIL = "13167000126@163.com";

    @Value("${file.upload-path:./data/uploads/}")
    private String uploadPath;

    @Override
    public Result register(RegisterDTO dto) {
        // 检查邮箱是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, dto.getEmail());
        SysUser existUser = sysUserMapper.selectOne(wrapper);
        if (existUser != null) {
            return Result.error(500, "该邮箱已被注册");
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setEmail(dto.getEmail());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setStatus(1);

        // 管理员邮箱自动设为管理员
        if (ADMIN_EMAIL.equals(dto.getEmail())) {
            user.setIsAdmin(1);
        } else {
            user.setIsAdmin(0);
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        sysUserMapper.insert(user);

        // 自动创建默认分组
        MemoGroup defaultGroup = new MemoGroup();
        defaultGroup.setUserId(user.getId());
        defaultGroup.setName("默认分组");
        defaultGroup.setIsDefault(1);
        defaultGroup.setSortOrder(0);
        defaultGroup.setCreatedAt(LocalDateTime.now());
        defaultGroup.setUpdatedAt(LocalDateTime.now());
        memoGroupMapper.insert(defaultGroup);

        return Result.success("注册成功");
    }

    @Override
    public Result login(LoginDTO dto) {
        // 根据邮箱查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, dto.getEmail());
        SysUser user = sysUserMapper.selectOne(wrapper);
        if (user == null) {
            return Result.error(500, "邮箱或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            return Result.error(500, "账号已被禁用");
        }

        // 验证密码
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            return Result.error(500, "邮箱或密码错误");
        }

        // 登录
        StpUtil.login(user.getId());

        // 返回token
        return Result.success(StpUtil.getTokenValue());
    }

    @Override
    public Result getProfile() {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            return Result.error(500, "用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    @Override
    public Result updateProfile(UserProfileDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            return Result.error(500, "用户不存在");
        }

        if (dto.getNickname() != null) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        user.setUpdatedAt(LocalDateTime.now());
        sysUserMapper.updateById(user);

        return Result.success("更新成功");
    }

    @Override
    public Result uploadAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error(500, "请选择要上传的文件");
        }

        // 生成UUID文件名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = IdUtil.simpleUUID() + extension;

        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        try {
            file.transferTo(new File(uploadDir, filename));
        } catch (IOException e) {
            return Result.error(500, "文件上传失败: " + e.getMessage());
        }

        // 返回访问路径
        String avatarUrl = "/api/v1/files/" + filename;
        return Result.success(avatarUrl);
    }
}
