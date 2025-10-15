package com.lilac.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (Role)表实体类
 *
 * @author lilac
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;
    //角色名称
    private String roleName;
    //角色标识
    private String roleKey;
    //角色描述
    private String remark;
    //状态(0正常，1禁用)
    private String status;
    //逻辑删除(0未删除，1已删除)
    private String delFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

