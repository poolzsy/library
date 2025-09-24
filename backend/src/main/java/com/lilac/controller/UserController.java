package com.lilac.controller;

import com.lilac.domain.dto.PageDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.result.Result;
import com.lilac.domain.vo.PageVO;
import com.lilac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
    public Result list(Integer id) {
        log.info("用户列表");
        // TODO 封装问题
        return Result.success(userService.selectById(id));
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/listAll")
    public Result<List<User>> listAll() {
        log.info("查询所有用户");
        List<User> users = userService.listAll();
        return Result.success(users);
    }

    /**
     * 新增用户信息
     */
    @PostMapping("/save")
    public Result<String> post(@RequestBody UserDTO userDTO) {
        log.info("新增用户信息");
        userService.save(userDTO);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        log.info("删除用户");
        userService.delete(id);
        return Result.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody UserDTO userDTO) {
        log.info("修改用户信息");
        userService.update(userDTO);
        // TODO id
        return Result.success();
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/list")
    public Result page(PageDTO pageDTO) {
        log.info("将用户分页展示");
        PageVO pagevo = userService.page(pageDTO);
        return Result.success(pagevo);
    }
}























