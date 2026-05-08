package com.memo.service;

import com.memo.common.Result;
import com.memo.dto.GroupDTO;

public interface GroupService {

    /**
     * 查询当前用户的分组列表
     *
     * @return 分组列表
     */
    Result list();

    /**
     * 创建分组
     *
     * @param dto 分组信息
     * @return 操作结果
     */
    Result create(GroupDTO dto);

    /**
     * 更新分组
     *
     * @param id  分组ID
     * @param dto 分组信息
     * @return 操作结果
     */
    Result update(Long id, GroupDTO dto);

    /**
     * 删除分组（非默认分组），该分组下的备忘录移到默认分组
     *
     * @param id 分组ID
     * @return 操作结果
     */
    Result delete(Long id);
}
