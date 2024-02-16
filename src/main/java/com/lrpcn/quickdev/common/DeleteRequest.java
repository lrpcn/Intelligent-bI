package com.lrpcn.quickdev.common;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/9 1:07
 */
@Data
public class DeleteRequest implements Serializable {

    @NotNull(message = "id不能为空")
    @Min(value = 0, message = "id不能小于0")
    private Long id;

    private static final long serialVersionUID = 1L;
}
