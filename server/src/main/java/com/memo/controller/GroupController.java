package com.memo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.memo.common.Result;
import com.memo.dto.GroupDTO;
import com.memo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 分组控制器
 */
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 查询当前用户的分组列表
     */
    @SaCheckLogin
    @GetMapping
    public Result list() {
        return groupService.list();
    }

    /**
     * 创建分组
     */
    @SaCheckLogin
    @PostMapping
    public Result create(@RequestBody GroupDTO dto) {
        return groupService.create(dto);
    }

    /**
     * 更新分组
     */
    @SaCheckLogin
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody GroupDTO dto) {
        return groupService.update(id, dto);
    }

    /**
     * 删除分组
     */
    @SaCheckLogin
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return groupService.delete(id);
    }
}
