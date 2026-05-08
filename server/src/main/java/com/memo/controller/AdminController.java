package com.memo.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.memo.common.Result;
import com.memo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 分页查询用户列表
     */
    @SaCheckRole("admin")
    @GetMapping("/users")
    public Result userList(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int pageSize,
                           @RequestParam(required = false) String keyword) {
        return adminService.userList(page, pageSize, keyword);
    }

    /**
     * 更新用户状态（禁用/启用）
     */
    @SaCheckRole("admin")
    @PutMapping("/users/{id}/status")
    public Result updateUserStatus(@PathVariable Long id,
                                   @RequestParam Integer status) {
        return adminService.updateUserStatus(id, status);
    }
}
