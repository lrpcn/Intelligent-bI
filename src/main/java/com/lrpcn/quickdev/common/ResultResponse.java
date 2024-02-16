package com.lrpcn.quickdev.common;

import com.lrpcn.quickdev.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能: 接口统一返回结果
 * 作者: lrpcn
 * 日期: 2024/2/8 19:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> {

    private Integer code;

    private String msg;

    private T data;

    public static <T> ResultResponse<T> success() {
        return new ResultResponse<>(200, "ok", null);
    }

    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(200, "ok", data);
    }

    public static <T> ResultResponse<T> error(ErrorCodeEnum errorCodeEnum) {
        return ResultResponse.<T>builder()
                .code(errorCodeEnum.getCode())
                .msg(errorCodeEnum.getMsg())
                .build();
    }

    public static <T> ResultResponse<T> error(ErrorCodeEnum errorCodeEnum, String string) {
        return ResultResponse.<T>builder()
                .code(errorCodeEnum.getCode())
                .msg(string)
                .build();
    }

    public static <T> ResultResponse<T> error(CustomException e) {
        return ResultResponse.<T>builder()
                .code(e.getCode())
                .msg(e.getMessage())
                .build();
    }


}
