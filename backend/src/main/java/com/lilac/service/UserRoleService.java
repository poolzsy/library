package com.lilac.service;

import com.lilac.domain.entity.UserRole;

import java.util.List;

/**
 * (UserRole)表服务接口
 *
 * @author lilac
 */
public interface UserRoleService {
    /**
     * 批量保存用户角色关系
     */
    void saveUserRole(List<UserRole> userRoleList);

    /**
     * 查询用户角色关系
     */
    List<UserRole> listUserRole(Integer id);

    /**
     * 删除用户角色关系
     */
    void removeByUserId(Integer id);

    /**
     * 删除用户角色关系
     */
    void removeByRoleId(Integer id);
}
