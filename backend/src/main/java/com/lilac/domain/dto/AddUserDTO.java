package com.lilac.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {
    private String userName;
    private String nickName;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private List<Integer> roleIds;
}
