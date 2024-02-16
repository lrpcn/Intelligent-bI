package com.lrpcn.quickdev.constant;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 角色枚举类，定义了不同角色及其对应权限列表
 * todo 应该根据数据库来规定
 */
@Getter
public enum RoleConstant {
    /**
     * 用户角色，拥有 "user" 权限
     */
    user("user", Collections.singletonList("user")),

    /**
     * 管理员角色，拥有 "admin" 和 "user" 权限
     */
    admin("admin", Collections.singletonList("*"));

    private final String role;

    private final List<String> roleList;

    RoleConstant(String role, List<String> roleList) {
        this.role = role;
        this.roleList = roleList;
    }
}
