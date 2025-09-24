package com.lilac.service;

import com.lilac.domain.dto.PageDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.result.Result;
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
    Result selectById(Integer id);

    /**
     * 查询所有用户
     */
    List listAll();

    /**
     * 新增用户信息
     */
    void save(UserDTO userDTO);

    /**
     * 删除用户
     */
    void delete(Integer id);

    /**
     * 修改用户信息
     */
    void update(UserDTO userDTO);

    /**
     * 分页查询用户
     */
    PageVO page(PageDTO pageDTO);
}
