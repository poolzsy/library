package com.lilac.filter;

import com.alibaba.fastjson.JSON;
import com.lilac.constant.ExceptionConstant;
import com.lilac.constant.SystemConstant;
import com.lilac.domain.entity.LoginUser;
import com.lilac.domain.result.Result;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.utils.JwtUtils;
import com.lilac.utils.RedisCache;
import com.lilac.utils.WebUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * 登录认证过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    /**
     * 拦截请求，进行token的校验
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从"Authorization" 请求头获取 token 字符串
        String authHeader = request.getHeader("Authorization");

        // 校验 "Authorization" 头是否存在且格式正确 (以 "Bearer " 开头)
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            // 如果没有 token 或格式不正确，直接放行，后续过滤器会处理
            filterChain.doFilter(request, response);
            return;
        }
        // 提取真正的 token ("Bearer " 后面部分)
        String token = authHeader.substring(7);

        // 解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // token超时 token非法
            Result result = Result.error(HttpsCodeEnum.NEED_LOGIN, ExceptionConstant.TOKEN_OVER_TIMEOUT);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        String userid = claims.getSubject();
        // 从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject(SystemConstant.USER_LOGIN_KEY + userid);

        // 存入SecurityContextHolder
        if (Objects.isNull(loginUser)) {
            Result result = Result.error(HttpsCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
