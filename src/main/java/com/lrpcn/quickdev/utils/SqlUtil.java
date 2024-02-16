package com.lrpcn.quickdev.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/9 16:54
 */
public class SqlUtil {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StrUtil.containsAny(sortField, "=", "(", ")", " ");
    }
}
