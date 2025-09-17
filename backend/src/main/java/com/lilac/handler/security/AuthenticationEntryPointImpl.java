package com.lilac.handler.security;

import com.lilac.domain.result.Result;
import com.lilac.enums.HttpsCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        Result result = null;
        if (authException instanceof BadCredentialsException){
            result = Result.error(HttpsCodeEnum.SYSTEM_ERROR, "用户名或密码错误");
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = Result.error(HttpsCodeEnum.SYSTEM_ERROR, "缺少权限");
        }else {
            result = Result.error(HttpsCodeEnum.SYSTEM_ERROR, "认证或授权失败");
        }
        // 响应给前端
    }
}
