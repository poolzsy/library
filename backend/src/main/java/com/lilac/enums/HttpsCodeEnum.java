package com.lilac.enums;

import lombok.Getter;

@Getter
public enum HttpsCodeEnum {
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统内部错误，请联系管理员"),

    BAD_REQUEST(400, "无效的请求参数"),
    UNAUTHORIZED(401, "未授权"),
    NEED_LOGIN(400001, "需要登录"),
    USER_OR_PASSWORD_ERROR(400002, "用户名或密码错误"),
    USER_EXIST(400003, "用户已存在"),
    DATA_EXIST(400004, "数据已存在"),
    DATA_NOT_EXIST(400005, "数据不存在"),
    DATA_ERROR(400006, "数据错误"),
    DATA_NOT_EMPTY(400007, "数据不能为空"),
    RESOURCE_NOT_FOUND(404, "请求的资源不存在");

    private final Integer code;
    private final String msg;

    HttpsCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
