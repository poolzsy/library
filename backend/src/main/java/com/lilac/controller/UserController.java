package com.lilac.controller;

import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.result.Result;
import com.lilac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录");
        String token = userService.login(userLoginDTO);
        Map<String, String> map = Collections.singletonMap("token", token);
        return Result.success(map);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户注册");
        userService.register(userLoginDTO);
        return Result.success();
    }
}
