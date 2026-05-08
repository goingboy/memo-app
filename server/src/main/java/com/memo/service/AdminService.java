package com.memo.service;

import com.memo.common.Result;

public interface AdminService {

    /**
     * 分页查询用户列表，支持关键词搜索
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param keyword  搜索关键词（模糊匹配email或nickname）
     * @return 用户分页列表
     */
    Result userList(int page, int pageSize, String keyword);

    /**
     * 更新用户状态（禁用/启用）
     *
     * @param id     用户ID
     * @param status 状态值（0-禁用, 1-正常）
     * @return 操作结果
     */
    Result updateUserStatus(Long id, Integer status);
}
