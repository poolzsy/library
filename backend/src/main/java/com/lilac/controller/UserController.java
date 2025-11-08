package com.lilac.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.lilac.constant.SystemConstant;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.AddUserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.LoginUser;
import com.lilac.domain.entity.User;
import com.lilac.domain.result.Result;
import com.lilac.domain.vo.PageVO;
import com.lilac.domain.vo.UserVO;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.service.UserService;
import com.lilac.utils.BeanCopyUtils;
import com.lilac.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCache;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录");
        // 从 Redis 中获取正确的验证码
        String captchaKey = SystemConstant.CAPTCHA_KEY_PREFIX + userLoginDTO.getCaptchaId();
        String correctCaptcha = redisCache.getCacheObject(captchaKey);
        // 立即删除 Redis 中的验证码，无论对错，防止重复使用
        redisCache.deleteObject(captchaKey);
        // 验证码校验
        if (correctCaptcha == null) {
            return Result.error(HttpsCodeEnum.BAD_REQUEST, "验证码已过期");
        }
        if (!correctCaptcha.equalsIgnoreCase(userLoginDTO.getCaptcha())) {
            return Result.error(HttpsCodeEnum.BAD_REQUEST, "验证码错误");
        }
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
     * 根据id查询修角色信息
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
    public Result save(@RequestBody AddUserDTO addUserDTO) {
        log.info("新增用户");
        userService.save(addUserDTO);
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
    public Result page(UserDTO userDTO) {
        log.info("分页查询");
        PageVO pagevo = userService.page(userDTO);
        return Result.success(pagevo);
    }

    /**
     * 查询当前登录用户信息
     */
    @GetMapping("/info")
    public Result pageInfo() {
        log.info("查询当前登录用户信息");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        UserVO userVO = BeanCopyUtils.copyBean(loginUser.getUser(), UserVO.class);
        return Result.success(userVO);
    }

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> getCaptcha() {
        log.info("获取验证码");
        // 创建验证码实例
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 4, 40);
        String code = captcha.getCode(); // 验证码文本
        String imageBase64 = captcha.getImageBase64(); // 图片的 Base64 编码
        // 生成一个唯一的 captchaId (UUID)
        String captchaId = IdUtil.simpleUUID();
        String captchaKey = SystemConstant.CAPTCHA_KEY_PREFIX + captchaId;
        // 将验证码存入 Redis，有效期 1 分钟
        redisCache.setCacheObject(captchaKey, code, 1, TimeUnit.MINUTES);
        // 将 captchaId 和图片数据返回给前端
        Map<String, String> data = new HashMap<>();
        data.put("captchaId", captchaId);
        data.put("captchaImage", "data:image/png;base64," + imageBase64);

        return Result.success(data);
    }

}
