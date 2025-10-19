package com.lilac.mapper;

import com.lilac.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

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
}
