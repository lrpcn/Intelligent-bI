package com.lrpcn.quickdev.common;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
    SUCCESS(200, "成功"),
    PARAMETER_ERROR(40000, "参数错误"),
    ACCOUNT_ALREADY_EXISTS(40000, "账号已经存在"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    TOO_MANY_REQUEST(42900,"请求过于频繁"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
