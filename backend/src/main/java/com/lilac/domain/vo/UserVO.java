package com.lilac.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Integer id;
    //用户名
    private String userName;
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
}
