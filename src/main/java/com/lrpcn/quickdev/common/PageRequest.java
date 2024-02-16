package com.lrpcn.quickdev.common;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/9 12:07
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    @Min(value = 1, message = "当前页号不能小于1")
    private int current = 1;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "当前页号不能小于1")
    @Max(value = 20, message = "当前页号不能小于1")
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = SORT_ORDER_ASC;

    /**
     * 升序
     */
    public static final String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    public static final String SORT_ORDER_DESC = " descend";
}
