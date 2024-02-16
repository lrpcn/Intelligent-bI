package com.lrpcn.quickdev.model.dto.chart;

import com.lrpcn.quickdev.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/11 10:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图标类型
     */
    private String chartType;

    /**
     * 用户名
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
