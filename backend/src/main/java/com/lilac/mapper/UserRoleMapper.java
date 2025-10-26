package com.lilac.mapper;

import com.lilac.domain.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (UserRole)表数据库访问层
 *
 * @author lilac
 */
@Mapper
public interface UserRoleMapper {
    /**
     * 批量保存用户角色关系
     */
    void saveBatch(List<UserRole> userRoleList);

    /**
     * 批量删除用户角色关系
     */
    @Delete("delete from user_role where user_id = #{id}")
    void remove(Integer id);

    /**
     * 批量查询用户角色关系
     */
    @Select("select * from user_role where user_id = #{userId}")
    List<UserRole> listUserRole(Integer userId);
}
