package com.lilac.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserRole)表实体类
 *
 * @author lilac
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    //用户id
    private Integer userId;
    //角色id
    private Integer roleId;

}

