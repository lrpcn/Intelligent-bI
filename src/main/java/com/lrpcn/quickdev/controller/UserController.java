package com.lrpcn.quickdev.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrpcn.quickdev.annotation.MyRateLimiter;
import com.lrpcn.quickdev.common.DeleteRequest;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.common.ResultResponse;
import com.lrpcn.quickdev.model.domain.User;
import com.lrpcn.quickdev.model.dto.user.UserAddRequest;
import com.lrpcn.quickdev.model.dto.user.UserEditRequest;
import com.lrpcn.quickdev.model.dto.user.UserQueryRequest;
import com.lrpcn.quickdev.model.dto.user.UserUpdateRequest;
import com.lrpcn.quickdev.service.UserService;
import com.lrpcn.quickdev.utils.bean.UserConversionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/8 22:34
 */

@Slf4j
@RestController
@SaCheckLogin
@Api("用户相关接口")
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 添加接口
     *
     * @param request
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("添加用户")
    @PostMapping("/add")
    public ResultResponse<Long> addUser(@RequestBody @Validated UserAddRequest request) {
        return ResultResponse.success(userService.addUser(request));
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("删除用户")
    @PostMapping("/del")
    public ResultResponse<Void> delUser(@RequestBody @Validated DeleteRequest deleteRequest) {
        userService.removeById(deleteRequest.getId());
        return ResultResponse.success();
    }

    /**
     * 更新接口
     *
     * @param request
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("更新用户信息")
    @PostMapping("/update")
    public ResultResponse<Void> updateUser(@RequestBody @Validated UserUpdateRequest request) {
        User user = UserConversionUtil.toUser(request);
        if (ObjUtil.isNull(user.getUpdateTime())) {
            user.setUpdateTime(new Date());
        }
        userService.updateById(user);
        return ResultResponse.success();
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("获取用户信息")
    @GetMapping("/get/userInfo/{userId}")
    public ResultResponse<User> getUserInfo(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        if (ObjUtil.isNull(user)) {
            return ResultResponse.error(ErrorCodeEnum.NOT_FOUND_ERROR);
        }
        return ResultResponse.success(user);
    }

    /**
     * 分页查询
     *
     * @param queryRequest
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("分页查询")
    @PostMapping("/list/page")
    public ResultResponse<Page<User>> getUserList(@RequestBody @Validated UserQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<User> postPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(queryRequest));
        return ResultResponse.success(postPage);
    }

    /**
     * 用户编辑接口
     *
     * @param request
     * @return
     */
    @MyRateLimiter(resourceId = "/api/user/edit", qps = 5.0)
    @SaCheckRole("user")
    @ApiOperation("用户编辑用户信息")
    @PostMapping("/edit")
    public ResultResponse<Void> editUser(@RequestBody @Validated UserEditRequest request) {
        User user = UserConversionUtil.toUser(request);
        user.setId(StpUtil.getLoginIdAsLong());
        user.setCreateTime(new Date());
        userService.updateById(user);
        return ResultResponse.success();
    }

}
