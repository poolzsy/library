package com.lilac.controller;

import com.lilac.domain.dto.PageDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.User;
import com.lilac.domain.result.Result;
import com.lilac.domain.vo.PageVO;
import com.lilac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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

    /**
     * 根据id查询修用户
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        log.info("根据ID查询用户: {}", id);
        return Result.success(userService.selectById(id));
    }

    /**
     * 新增用户信息
     */
    @PostMapping("/save")
    public Result save(@RequestBody UserDTO userDTO) {
        log.info("新增用户");
        userService.save(userDTO);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除用户");
        userService.delete(id);
        return Result.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        log.info("修改用户信息");
        userService.update(user);
        return Result.success();
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/list")
    public Result page(PageDTO pageDTO) {
        log.info("分页查询");
        PageVO pagevo = userService.page(pageDTO);
        return Result.success(pagevo);
    }
}























