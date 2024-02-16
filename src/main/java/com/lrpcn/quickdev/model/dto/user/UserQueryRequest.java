package com.lrpcn.quickdev.model.dto.user;

import com.lrpcn.quickdev.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/9 16:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {

    private Date createTime;

    private static final long serialVersionUID = 1L;
}
