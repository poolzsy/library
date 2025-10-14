package com.lilac.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色DTO
 * @author lilac
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Integer pageNum;
    private Integer pageSize;
    private String roleName;
}
