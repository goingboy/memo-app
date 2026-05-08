package com.memo.service;

import com.memo.common.Result;
import com.memo.dto.LoginDTO;
import com.memo.dto.RegisterDTO;
import com.memo.dto.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    /**
     * 用户注册
     *
     * @param dto 注册信息（邮箱、密码、昵称）
     * @return 操作结果
     */
    Result register(RegisterDTO dto);

    /**
     * 用户登录
     *
     * @param dto 登录信息（邮箱、密码）
     * @return 操作结果，包含token
     */
    Result login(LoginDTO dto);

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    Result getProfile();

    /**
     * 更新当前用户个人资料
     *
     * @param dto 个人资料（昵称、头像）
     * @return 操作结果
     */
    Result updateProfile(UserProfileDTO dto);

    /**
     * 上传用户头像
     *
     * @param file 头像文件
     * @return 操作结果，包含头像访问URL
     */
    Result uploadAvatar(MultipartFile file);
}
