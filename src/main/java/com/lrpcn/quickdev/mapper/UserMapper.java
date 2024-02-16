package com.lrpcn.quickdev.mapper;

import com.lrpcn.quickdev.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【user(`user`)】的数据库操作Mapper
* @createDate 2024-02-08 19:20:58
* @Entity com.lrpcn.quickdev.model.domain.UserRegisterRequest
*/
public interface UserMapper extends BaseMapper<User> {

    User selectByUserName(String userAccount);


}




