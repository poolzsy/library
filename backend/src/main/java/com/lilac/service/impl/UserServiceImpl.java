package com.lilac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.domain.dto.PageDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.User;
import com.lilac.domain.result.Result;
import com.lilac.domain.vo.PageVO;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.UserMapper;
import com.lilac.service.UserService;
import com.lilac.utils.JwtUtils;
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
            throw new SystemException(HttpsCodeEnum.USER_OR_PASSWORD_ERROR);
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
    public Result selectById(Integer id) {
        User user = userMapper.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @Override
    public List listAll() {
        return userMapper.listAll();
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
        userMapper.deleteByid(id);
    }

    /**
     * 修改用户信息
     *
     * @param userDTO
     */
    @Override
    public void update(UserDTO userDTO) {
        if (userMapper.findByUsername(userDTO.getUsername()) != null) {
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        } else {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            setPassword(user);
            userMapper.update(user);
        }
    }

    /**
     * 分页查询用户
     *
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageVO page(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<User> userList = userMapper.page(pageDTO);
        // TODO 待修改
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return new PageVO(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 设置默认密码
     */
    public void setPassword(User user){
        if(user.getPassword() == null){
            String hashedPassword = passwordEncoder.encode("123456");
        }else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }

}