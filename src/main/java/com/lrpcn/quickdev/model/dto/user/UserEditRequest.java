package com.lrpcn.quickdev.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/8 22:28
 */
@ApiModel(value = "用户编辑请求")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditRequest implements Serializable {

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 1, max = 10, message = "用户昵称长度不能超过10和低于1")
    private String userName;

    private static final long serialVersionUID = 1L;
}
