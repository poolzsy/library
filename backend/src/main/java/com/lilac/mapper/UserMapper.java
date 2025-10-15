package com.lilac.mapper;

import com.lilac.annotation.AutoFill;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.entity.User;
import com.lilac.enums.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author lilac
 */
@Mapper
public interface UserMapper{

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户
     */
    @Select("select * from user where user_name = #{userName} and status = 0 and del_flag = 0")
    User findByUserName(String userName);

    /**
     * 保存用户
     *
     * @param user 用户
     */
    @AutoFill(OperationType.INSERT)
    void save(User user);

    /**
     * 根据id查询用户
     *
     * @return 用户
     */
    @Select("select * from user where id = #{id} and status = 0 and del_flag = 0")
    User selectById(Integer id);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    @Update("update user set del_flag = 1 where id = #{id}")
    void deleteById(Integer id);

    /**
     * 修改用户
     *
     * @param user 用户
     */
    @AutoFill(OperationType.UPDATE)
    void update(User user);

    /**
     * 分页查询用户
     *
     * @return 用户列表
     */
    List<User> pageList(UserDTO userDTO);
}
