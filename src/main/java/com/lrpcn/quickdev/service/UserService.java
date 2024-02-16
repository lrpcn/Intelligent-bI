package com.lrpcn.quickdev.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.lrpcn.quickdev.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrpcn.quickdev.model.dto.user.UserAddRequest;
import com.lrpcn.quickdev.model.dto.user.UserLoginRequest;
import com.lrpcn.quickdev.model.dto.user.UserQueryRequest;

/**
* @author Administrator
* @description 针对表【user(`user`)】的数据库操作Service
* @createDate 2024-02-08 19:20:58
*/
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param dto 登录信息
     */
    void login(UserLoginRequest dto);

    /**
     * 添加用户
     * @param request
     * @return
     */
    Long addUser(UserAddRequest request);

    Wrapper<User> getQueryWrapper(UserQueryRequest queryRequest);
}
