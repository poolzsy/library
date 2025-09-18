package com.lilac.service.impl;

import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.User;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.UserMapper;
import com.lilac.service.UserService;
import com.lilac.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author lilac
 */
@SuppressWarnings({"all"})
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 登录
     *
     * @param loginDTO 用户信息
     * @return 登录结果
     */
    @Override
    public String login(UserLoginDTO loginDTO) {
        // 解密前端加密的密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        // 调用认证管理器进行认证
        Authentication authenticate;
        try {
            // 调用认证管理器进行认证
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new SystemException(HttpsCodeEnum.SYSTEM_ERROR);
        }
        // 生成token
        return jwtUtils.generateToken(authenticate.getName());
    }


    /**
     * 注册
     *
     * @param loginDTO 包含加密密码的DTO
     */
    @Override
    public void register(UserLoginDTO loginDTO) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(loginDTO.getUsername()) != null) {
            throw new SystemException(HttpsCodeEnum.SYSTEM_ERROR);
        }
        // 使用BCrypt对明文密码进行哈希处理
        String hashedPassword = passwordEncoder.encode(loginDTO.getPassword());
        // 创建用户实体并存入数据库
        User newUser = new User();
        newUser.setUsername(loginDTO.getUsername());
        newUser.setPassword(hashedPassword);

        userMapper.save(newUser);
    }
}