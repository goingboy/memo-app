package com.memo.service;

import com.memo.common.Result;
import com.memo.dto.MemoDTO;

public interface MemoService {

    /**
     * 分页查询备忘录列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param groupId  分组ID（可选筛选）
     * @return 备忘录分页列表
     */
    Result list(int page, int pageSize, Long groupId);

    /**
     * 查询备忘录详情
     *
     * @param id 备忘录ID
     * @return 备忘录详情
     */
    Result detail(Long id);

    /**
     * 创建备忘录
     *
     * @param dto 备忘录信息
     * @return 操作结果
     */
    Result create(MemoDTO dto);

    /**
     * 更新备忘录
     *
     * @param id  备忘录ID
     * @param dto 备忘录信息
     * @return 操作结果
     */
    Result update(Long id, MemoDTO dto);

    /**
     * 软删除备忘录
     *
     * @param id 备忘录ID
     * @return 操作结果
     */
    Result delete(Long id);

    /**
     * 查询垃圾站列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 已删除的备忘录分页列表
     */
    Result trashList(int page, int pageSize);

    /**
     * 恢复已删除的备忘录
     *
     * @param id 备忘录ID
     * @return 操作结果
     */
    Result restore(Long id);

    /**
     * 永久删除备忘录
     *
     * @param id 备忘录ID
     * @return 操作结果
     */
    Result forceDelete(Long id);
}
