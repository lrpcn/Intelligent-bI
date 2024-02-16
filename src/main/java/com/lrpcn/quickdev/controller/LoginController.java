package com.lrpcn.quickdev.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrpcn.quickdev.annotation.MyRateLimiter;
import com.lrpcn.quickdev.annotation.MyRedisLimiter;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.common.ResultResponse;
import com.lrpcn.quickdev.exception.ThrowUtil;
import com.lrpcn.quickdev.model.domain.User;
import com.lrpcn.quickdev.model.dto.user.UserLoginRequest;
import com.lrpcn.quickdev.model.dto.user.UserRegisterRequest;
import com.lrpcn.quickdev.model.vo.LoginUserVO;
import com.lrpcn.quickdev.service.UserService;
import com.lrpcn.quickdev.utils.OssFileUtil;
import com.lrpcn.quickdev.utils.bean.UserConversionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/8 18:58
 */
@Slf4j
@RestController
@Api("用户登录")
@RequestMapping("/api/user")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private OssFileUtil fileUtil;

//    private RateLimiter rateLimiter = RateLimiter.create(10,1, TimeUnit.SECONDS);

    /**
     * 用户登录接口
     *
     * @param dto 登录信息
     * @return 登录结果
     */
    @SaIgnore
//    @MyRateLimiter(resourceId = "/api/auth/login", qps = 3.0,warmupPeriod = 1)
    @MyRedisLimiter(key = "key:login", qps = 4)
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResultResponse<Void> login(@RequestBody @Validated UserLoginRequest dto) {
        userService.login(dto);
        return ResultResponse.success();
    }

    @SaIgnore
//    @MyRateLimiter(resourceId = "/api/auth/login", qps = 3.0,warmupPeriod = 1)
    @MyRedisLimiter(key = "key:get:login", qps = 4)
    @ApiOperation("用户登录")
    @GetMapping("/get/login")
    public ResultResponse<LoginUserVO> getLoginUser() {
        long id = StpUtil.getLoginIdAsLong();
        User user = userService.getById(id);
        LoginUserVO vo = UserConversionUtil.toLoginUserVO(user);
        return ResultResponse.success();
    }

    /**
     * 测试接口
     */
    @SaIgnore
    @MyRedisLimiter(key = "key:test", qps = 1)
    @ApiOperation("测试接口")
    @PostMapping("/test")
    public ResultResponse<String> test() {
        return ResultResponse.success("test");
    }

    /**
     * 登出接口
     *
     * @return 登出结果
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public ResultResponse<Boolean> logout() {
        StpUtil.logout();
        return ResultResponse.success(true);
    }

    /**
     * 用户注册接口
     *
     * @param file    头像
     * @param request
     * @return
     * @throws IOException
     */
    @SaIgnore
    @MyRateLimiter(resourceId = "/api/auth/register", qps = 4)
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResultResponse<Void> register(MultipartFile file,
                                         @RequestBody @Validated UserRegisterRequest request) throws IOException {
        User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, request.getUserAccount()));
        ThrowUtil.throwIfCustomException(ObjUtil.isNotNull(one), ErrorCodeEnum.ACCOUNT_ALREADY_EXISTS);
        String avatarUrl = fileUtil.uploadPicture(file);
        User user = UserConversionUtil.toUser(request);
        user.setUserAvatar(avatarUrl);
        return ResultResponse.success();
    }

}
