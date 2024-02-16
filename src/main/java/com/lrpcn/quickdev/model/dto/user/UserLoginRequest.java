package com.lrpcn.quickdev.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/8 18:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest implements Serializable {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 15, message = "用户名长度不能超过15和低于6")
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度不能超过20和低于6")
    private String userPassword;

    private static final long serialVersionUID = 1L;

}
