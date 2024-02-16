package com.lrpcn.quickdev.exception;

import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.exception.CustomException;

/**
 * 功能: 异常工具类
 * 作者: lrpcn
 * 日期: 2024/2/8 20:59
 */
public class ThrowUtil {

    /**
     * 根据条件判断是否抛出自定义异常
     * @param b 判断条件
     * @param errorCodeEnum 错误码枚举
     */
    public static void throwIfCustomException(boolean b, ErrorCodeEnum errorCodeEnum) {
        if (b) {
            throw new CustomException(errorCodeEnum);
        }
    }

    /**
     * 根据条件判断是否抛出自定义异常
     * @param b 判断条件
     * @param code 错误码
     * @param msg 错误信息
     */
    public static void throwIfCustomException(boolean b, int code, String msg) {
        if (b) {
            throw new CustomException(code, msg);
        }
    }

    public static void throwIfCustomException(boolean b, ErrorCodeEnum errorCodeEnum, String msg) {
        if (b) {
            throw new CustomException(errorCodeEnum,msg);
        }
    }

    /**
     * 抛出自定义异常
     * @param errorCodeEnum 错误码枚举
     */
    public static void throwCustomException(ErrorCodeEnum errorCodeEnum) {
        throw new CustomException(errorCodeEnum);
    }

    public static void throwRuntimeException() {
        throw new RuntimeException();
    }


}
