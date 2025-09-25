package com.lilac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.constant.SystemConstant;
import com.lilac.domain.dto.PageDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.LoginUser;
import com.lilac.domain.entity.User;
import com.lilac.domain.vo.PageVO;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.UserMapper;
import com.lilac.service.UserService;
import com.lilac.utils.JwtUtils;
import com.lilac.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类
 *
 * @author lilac
 */
@SuppressWarnings({"all"})
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisCache redisCache;

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
            throw new SystemException(HttpsCodeEnum.USER_OR_PASSWORD_ERROR);
        }
        // 获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);

        // 存入redis
        redisCache.setCacheObject(SystemConstant.USER_LOGIN_KEY + userId, loginUser);
        return jwt;
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
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        }
        // 使用BCrypt对明文密码进行哈希处理
        String hashedPassword = passwordEncoder.encode(loginDTO.getPassword());
        // 创建用户实体并存入数据库
        User newUser = new User();
        newUser.setUsername(loginDTO.getUsername());
        newUser.setPassword(hashedPassword);

        userMapper.save(newUser);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     */
    @Override
    public User selectById(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }

    /**
     * 新增用户信息
     *
     * @param userSaveDTO
     */
    @Override
    public void save(UserDTO userDTO) {
        if (userMapper.findByUsername(userDTO.getUsername()) != null) {
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        } else {
            User newUser = new User();
            BeanUtils.copyProperties(userDTO, newUser);
            setPassword(newUser);
            userMapper.save(newUser);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 修改用户信息
     *
     * @param userDTO
     */
    @Override
    public void update(User user) {
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        }
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        // 只有当传入的密码不为空时才更新密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.update(newUser);
    }

    /**
     * 分页查询用户
     *
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageVO page(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageSize(), pageDTO.getPageNum());
        List<User> userList = userMapper.pageList(pageDTO);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        PageVO page = new PageVO(pageInfo.getTotal(), pageInfo.getList());
        return page;
    }

    /**
     * 设置默认密码
     */
    public void setPassword(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(SystemConstant.DEFINED_PASSWORD));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }

}