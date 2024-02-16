package com.lrpcn.quickdev.utils.bean;

import com.lrpcn.quickdev.constant.RoleConstant;
import com.lrpcn.quickdev.model.domain.User;
import com.lrpcn.quickdev.model.dto.user.UserAddRequest;
import com.lrpcn.quickdev.model.dto.user.UserEditRequest;
import com.lrpcn.quickdev.model.dto.user.UserRegisterRequest;
import com.lrpcn.quickdev.model.dto.user.UserUpdateRequest;
import com.lrpcn.quickdev.model.vo.LoginUserVO;

/**
 * 功能: 自定义bean转换
 * 作者: lrpcn
 * 日期: 2024/2/8 22:39
 */
public class UserConversionUtil {

    /**
     * UserAddRequest 转 UserRegisterRequest
     *
     * @param request
     * @return
     */
    public static User toUser(UserAddRequest request) {
        return User.builder()
                .id(null)
                .userAccount(request.getUserAccount())
                .userPassword(request.getUserPassword())
                .userName(request.getUserName())
                .userAvatar(null)
                .userRole(request.getUserRole())
                .createTime(null)
                .updateTime(null)
                .isDeleted(null)
                .build();

    }

    public static User toUser(UserUpdateRequest request) {
        return User.builder()
                .id(request.getId())
                .userAccount(request.getUserAccount())
                .userPassword(request.getUserPassword())
                .userName(request.getUserName())
                .userAvatar(request.getUserAvatar())
                .userRole(request.getUserRole())
                .createTime(request.getCreateTime())
                .updateTime(request.getUpdateTime())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    public static User toUser(UserEditRequest request) {
        return User.builder()
                .id(null)
                .userName(request.getUserName())
                .build();
    }

    public static User toUser(UserRegisterRequest request) {
        return User.builder()
                .id(null)
                .userAccount(request.getUserAccount())
                .userPassword(request.getUserPassword())
                .userName(request.getUserName())
                .userAvatar(null)
                .userRole(RoleConstant.user.getRole())
                .createTime(null)
                .updateTime(null)
                .isDeleted(null)
                .build();
    }

    public static LoginUserVO toLoginUserVO(User user) {
        return LoginUserVO.builder()
                .userAvatar(user.getUserAvatar())
                .userName(user.getUserName())
                .userRole(user.getUserRole())
                .userProfile("")
                .updateTime(user.getUpdateTime())
                .createTime(user.getCreateTime())
                .build();
    }
}
