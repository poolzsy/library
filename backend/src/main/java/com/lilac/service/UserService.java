package com.lilac.service;

import com.lilac.domain.dto.AddUserDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.User;
import com.lilac.domain.vo.PageVO;

import java.util.List;

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

    /**
     * 根据id查询用户
     */
    User selectById(Integer id);

    /**
     * 新增用户信息
     */
    void save(AddUserDTO addUserDTO);

    /**
     * 删除用户
     */
    void delete(Integer id);

    /**
     * 修改用户信息
     */
    void update(User user);

    /**
     * 分页查询用户
     */
    PageVO page(UserDTO userDTO);
}
