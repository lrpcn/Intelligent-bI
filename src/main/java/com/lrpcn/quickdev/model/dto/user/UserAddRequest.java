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
 * 日期: 2024/2/8 22:18
 */
@ApiModel(value = "用户添加请求")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest implements Serializable {

    /**
     * 账号
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 15, message = "用户名长度不能超过15和低于6")
    @ApiModelProperty(value = "账号")
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度不能超过20和低于6")
    @ApiModelProperty(value = "密码")
    private String userPassword;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 1, max = 10, message = "用户昵称长度不能超过10和低于1")
    @ApiModelProperty(value = "用户昵称")
    private String userName;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色")
    private String userRole;

    private static final long serialVersionUID = 1L;
}
