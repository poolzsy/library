package com.lilac.service.impl;

import com.lilac.domain.entity.UserRole;
import com.lilac.mapper.UserRoleMapper;
import com.lilac.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (UserRole)表服务实现类
 *
 * @author lilac
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 新增用户角色关联
     */
    @Override
    public void saveUserRole(List<UserRole> userRoleList) {
        userRoleMapper.saveBatch(userRoleList);
    }
}
