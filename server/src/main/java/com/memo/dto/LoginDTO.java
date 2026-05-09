package com.memo.dto;

import lombok.Data;

@Data
public class LoginDTO {

    /** 账号名 */
    private String username;

    private String password;
}
