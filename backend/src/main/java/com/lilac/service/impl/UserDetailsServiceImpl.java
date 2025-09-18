package com.lilac.service.impl;

import com.lilac.domain.entity.LoginUser;
import com.lilac.domain.entity.User;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (Objects.isNull(user)){
            throw new UsernameNotFoundException(HttpsCodeEnum.USER_OR_PASSWORD_ERROR.getMsg());
        }
        // TODO 权限
        return new LoginUser(user, null);
    }
}