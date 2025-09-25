package com.lilac.handler.security;

import com.alibaba.fastjson.JSON;
import com.lilac.domain.result.Result;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 在未认证或者认证错误的情况下访问需要认证的资源时的处理类
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        Result result = null;
        if (authException instanceof BadCredentialsException){
            result = Result.error(HttpsCodeEnum.USER_OR_PASSWORD_ERROR, authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = Result.error(HttpsCodeEnum.NEED_LOGIN);
        }else {
            result = Result.error(HttpsCodeEnum.SYSTEM_ERROR, "认证或授权失败");
        }
        // 响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}