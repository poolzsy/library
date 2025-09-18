package com.lilac.service;

import com.lilac.domain.dto.UserLoginDTO;

/**
 * (User)表服务接口
 *
 * @author lilac
 */
public interface UserService{
    /**
     * 登录
     */
    String login(UserLoginDTO userLoginDTO);

    /**
     * 注册
     */
    void register(UserLoginDTO userLoginDTO);
}
