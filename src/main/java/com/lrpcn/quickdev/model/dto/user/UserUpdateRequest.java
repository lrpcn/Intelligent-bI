package com.lrpcn.quickdev.model.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.lrpcn.quickdev.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/9 1:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserUpdateRequest extends User implements Serializable {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     * 用户昵称
     */
    @TableField(value = "userName")
    private String userName;

    /**
     * 用户头像
     */
    @TableField(value = "userAvatar")
    private String userAvatar;

    /**
     * 用户角色
     */
    @TableField(value = "userRole")
    private String userRole;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableField(value = "isDeleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
