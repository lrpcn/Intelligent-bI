package com.lrpcn.quickdev.manager;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.StrUtil;
import com.lrpcn.quickdev.constant.RoleConstant;
import com.lrpcn.quickdev.model.domain.User;
import com.lrpcn.quickdev.service.UserService;
import com.lrpcn.quickdev.utils.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.lrpcn.quickdev.constant.RedisKeyConstant.KEY_ROLE_BY_USERID;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    UserService userService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.singletonList("null");
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userRole = redisUtil.get(KEY_ROLE_BY_USERID);
        if (StrUtil.isBlank(userRole)) {
            Long id = Long.valueOf(loginId.toString());
            User user = userService.getById(id);
            userRole = user.getUserRole();
            redisUtil.set(KEY_ROLE_BY_USERID, userRole);
        }
        switch (userRole) {
            case "admin": {
                return RoleConstant.admin.getRoleList();
            }
            case "user": {
                return RoleConstant.user.getRoleList();
            }
            default:
                return RoleConstant.user.getRoleList();
        }
    }

}
