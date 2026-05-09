package com.memo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 账号名（唯一） */
    private String username;

    /** 绑定邮箱（唯一） */
    private String email;

    private String password;

    private String nickname;

    private String avatar;

    /** 0-普通用户, 1-管理员 */
    private Integer isAdmin;

    /** 0-禁用, 1-正常 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
