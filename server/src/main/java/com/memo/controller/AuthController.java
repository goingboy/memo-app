package com.memo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.memo.common.Result;
import com.memo.dto.LoginDTO;
import com.memo.dto.RegisterDTO;
import com.memo.dto.UserProfileDTO;
import com.memo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO dto) {
        return authService.register(dto);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO dto) {
        return authService.login(dto);
    }

    /**
     * 获取当前用户信息
     */
    @SaCheckLogin
    @GetMapping("/profile")
    public Result getProfile() {
        return authService.getProfile();
    }

    /**
     * 更新个人资料
     */
    @SaCheckLogin
    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserProfileDTO dto) {
        return authService.updateProfile(dto);
    }

    /**
     * 上传头像
     */
    @SaCheckLogin
    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        return authService.uploadAvatar(file);
    }
}
