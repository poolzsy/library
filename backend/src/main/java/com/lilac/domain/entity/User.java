package com.lilac.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * (User)表实体类
 *
 * @author lilac
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    //用户名
    private String userName;
    //密码
    private String password;
    //昵称
    private String nickName;
    //头像
    private String avatar;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //用户状态(0正常，1禁用)
    private String status;
    //用户类型(0普通用户，1管理员)
    private String type;
    //创建时间
    private LocalDateTime createTime;
    //修改时间
    private LocalDateTime updateTime;
    //逻辑删除(0未删除，1删除)
    private String delFlag;
    //角色id集合
    private List<Integer> roleIds;

}

