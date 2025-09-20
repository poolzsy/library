package com.lilac.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * (User)表实体类
 *
 * @author lilac
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickname;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //创建时间
    private LocalDateTime createTime;
    //修改时间
    private LocalDateTime updateTime;
    //逻辑删除(0未删除，1删除)
    private String delFlag;

}

