package com.lilac.mapper;

import com.lilac.annotation.AutoFill;
import com.lilac.domain.dto.RoleDTO;
import com.lilac.domain.entity.Role;
import com.lilac.enums.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (Role)表数据库访问层
 *
 * @author lilac
 */
@Mapper
public interface RoleMapper {
    /**
     * 通过id查询角色
     */
    @Select("select * from role where id = #{id} and del_flag = 0 ")
    Role SelectById(Integer id);

    /**
     * 查询角色
     */
    List<Role> page(RoleDTO roleDTO);

    /**
     * 新增角色
     */
    @AutoFill(OperationType.INSERT)
    void save(Role role);

    /**
     * 删除用户角色
     */
    @Update("update role set del_flag = 1 where id = #{id}")
    void deleteById(Integer id);

    /**
     * 修改角色信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Role role);

    /**
     * 查询所有角色
     */
    @Select("select * from role where del_flag = 0")
    List<Role> listAll();

    /**
     * 通过角色名查询角色
     */
    @Select("select * from role where role_name = #{roleName} and del_flag = 0")
    Role SelectByRoleName(String roleName);

    /**
     * 通过权限标识查询角色
     */
    @Select("select * from role where role_key = #{roleKey} and del_flag = 0")
    Role SelectByRoleKey(String roleKey);
}
