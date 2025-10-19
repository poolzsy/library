package com.lilac.service;

import com.lilac.domain.dto.AddRoleDTO;
import com.lilac.domain.dto.RoleDTO;
import com.lilac.domain.entity.Role;
import com.lilac.domain.vo.PageVO;

import java.util.List;

/**
 * (Role)表服务接口
 *
 * @author lilac
 */
public interface RoleService {
    /**
     * 根据id查询角色
     */
    Role SelectById(Integer id);

    /**
     * 查询角色
     */
    PageVO page(RoleDTO roleDTO);

    /**
     * 新增角色
     */
    void save(AddRoleDTO addRoleDTO);

    /**
     * 删除用户角色
     */
    void deleteRoleById(Integer id);

    /**
     * 修改角色信息
     */
    void update(Role role);

    /**
     * 查询所有角色
     */
    List<Role> listAll();
}
