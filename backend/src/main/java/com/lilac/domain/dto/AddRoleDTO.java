package com.lilac.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加角色的DTO
 * @author lilac
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDTO {
    //角色名称
    private String roleName;
    //角色标识
    private String roleKey;
    //角色描述
    private String remark;
    //角色状态
    private String status;
}
