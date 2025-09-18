package com.lilac.mapper;

import com.lilac.domain.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
     * @param username 用户名
     * @return 用户
     */
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    /**
     * 保存用户
     *
     * @param newUser 用户
     */
    @Insert("insert into user(username, password) values(#{username}, #{password})")
    void save(User newUser);
}
