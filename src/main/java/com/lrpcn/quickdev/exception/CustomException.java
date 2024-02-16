package com.lrpcn.quickdev.exception;

import com.lrpcn.quickdev.common.ErrorCodeEnum;
import lombok.Getter;

/**
 * 功能: 自定义异常
 * 作者: lrpcn
 * 日期: 2024/2/8 21:03
 */
@Getter
public class CustomException extends RuntimeException {
    /**
     * 状态码
     */
    private final int code;

    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public CustomException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMsg());
        this.code = errorCodeEnum.getCode();
    }

    public CustomException(ErrorCodeEnum errorCodeEnum, String msg) {
        super(msg);
        this.code = errorCodeEnum.getCode();
    }

    public static CustomException error(ErrorCodeEnum errorCodeEnum, String msg) {
        return new CustomException(errorCodeEnum, msg);
    }

    public static CustomException error(ErrorCodeEnum errorCodeEnum) {
        return new CustomException(errorCodeEnum);
    }

}
