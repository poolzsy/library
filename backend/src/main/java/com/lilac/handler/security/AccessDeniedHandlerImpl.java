//package com.lilac.handler.security;
//
//import com.alibaba.fastjson.JSON;
//import com.lilac.enums.AppHttpCodeEnum;
//import com.lilac.pojo.result.ResponseResult;
//import com.lilac.utils.WebUtils;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//
//@Component
//public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        accessDeniedException.printStackTrace();
//        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
//        // 响应给前端
//        WebUtils.renderString(response, JSON.toJSONString(result));
//    }
//}
