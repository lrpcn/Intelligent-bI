package com.lrpcn.quickdev.model.dto.chart;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/11 10:48
 */
@Data
public class ChartEditRequest implements Serializable {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
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
     * 图表数据
     */
    private String chartData;

    /**
     * 图标类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}
