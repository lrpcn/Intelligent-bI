package com.lrpcn.quickdev.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.mapper.UserMapper;
import com.lrpcn.quickdev.model.domain.User;
import com.lrpcn.quickdev.model.dto.user.UserAddRequest;
import com.lrpcn.quickdev.model.dto.user.UserLoginRequest;
import com.lrpcn.quickdev.model.dto.user.UserQueryRequest;
import com.lrpcn.quickdev.service.UserService;
import com.lrpcn.quickdev.utils.SqlUtil;
import com.lrpcn.quickdev.utils.bean.UserConversionUtil;
import com.lrpcn.quickdev.exception.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.lrpcn.quickdev.common.PageRequest.SORT_ORDER_ASC;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    UserMapper userMapper;

    private static final String SALT = "lrpcn";

    /**
     * 登录
     *
     * @param dto 登录信息
     */
    @Override
    public void login(UserLoginRequest dto) {
        String userAccount = dto.getUserAccount();
        String userPassword = DigestUtil.md5Hex(SALT + dto.getUserPassword());
        User user = userMapper.selectByUserName(userAccount);
        boolean b = StrUtil.equals(userPassword, user.getUserPassword());
        ThrowUtil.throwIfCustomException(!b, ErrorCodeEnum.PARAMETER_ERROR);
        StpUtil.login(user.getId());
    }

    /**
     * 添加用户
     *
     * @param request 添加用户信息
     * @return 用户id
     */
    @Override
    public Long addUser(UserAddRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, request.getUserAccount());
        User one = getOne(wrapper);
        ThrowUtil.throwIfCustomException(ObjUtil.isNotNull(one), ErrorCodeEnum.PARAMETER_ERROR, "数据已存在");
        User user = UserConversionUtil.toUser(request);
        user.setUserPassword(DigestUtil.md5Hex(SALT + user.getUserPassword()));
        boolean b = save(user);
        ThrowUtil.throwIfCustomException(!b, ErrorCodeEnum.SYSTEM_ERROR, "保存失败");
        return user.getId();
    }

    /**
     * 构建分页查询
     *
     * @param queryRequest
     * @return
     */
    @Override
    public Wrapper<User> getQueryWrapper(UserQueryRequest queryRequest) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();
        Date createTime = queryRequest.getCreateTime();
        wrapper.orderBy(SqlUtil.validSortField(sortField), StrUtil.equals(sortOrder, SORT_ORDER_ASC), sortField);
        return wrapper;
    }
}




