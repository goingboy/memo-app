package com.memo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    /** 账号名（3-20位，唯一） */
    @NotBlank(message = "账号名不能为空")
    @Size(min = 3, max = 20, message = "账号名长度3-20位")
    private String username;

    /** 绑定邮箱（唯一） */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /** 密码（至少6位） */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码至少6位")
    private String password;

    /** 昵称（可选） */
    private String nickname;
}
