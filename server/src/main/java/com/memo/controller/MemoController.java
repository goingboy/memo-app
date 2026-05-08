package com.memo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.memo.common.Result;
import com.memo.dto.MemoDTO;
import com.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 备忘录控制器
 */
@RestController
@RequestMapping("/api/v1/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    /**
     * 分页查询备忘录列表
     */
    @SaCheckLogin
    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) Long groupId) {
        return memoService.list(page, pageSize, groupId);
    }

    /**
     * 查询备忘录详情
     */
    @SaCheckLogin
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        return memoService.detail(id);
    }

    /**
     * 创建备忘录
     */
    @SaCheckLogin
    @PostMapping
    public Result create(@RequestBody MemoDTO dto) {
        return memoService.create(dto);
    }

    /**
     * 更新备忘录
     */
    @SaCheckLogin
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody MemoDTO dto) {
        return memoService.update(id, dto);
    }

    /**
     * 软删除备忘录
     */
    @SaCheckLogin
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return memoService.delete(id);
    }

    /**
     * 查询垃圾站列表
     */
    @SaCheckLogin
    @GetMapping("/trash")
    public Result trashList(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int pageSize) {
        return memoService.trashList(page, pageSize);
    }

    /**
     * 恢复已删除的备忘录
     */
    @SaCheckLogin
    @PostMapping("/{id}/restore")
    public Result restore(@PathVariable Long id) {
        return memoService.restore(id);
    }

    /**
     * 永久删除备忘录
     */
    @SaCheckLogin
    @DeleteMapping("/{id}/force")
    public Result forceDelete(@PathVariable Long id) {
        return memoService.forceDelete(id);
    }
}
