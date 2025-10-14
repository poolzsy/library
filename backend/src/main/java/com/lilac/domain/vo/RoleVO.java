package com.lilac.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色VO
 * @author lilac
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVO {
    private Integer id;
    //角色名称
    private String roleName;
    //角色标识
    private String roleKey;
    //角色描述
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
