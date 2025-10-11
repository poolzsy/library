package com.lilac.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
}
