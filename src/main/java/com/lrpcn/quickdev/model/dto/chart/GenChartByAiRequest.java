package com.lrpcn.quickdev.model.dto.chart;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/11 10:48
 */
@Data
public class GenChartByAiRequest implements Serializable {

    /**
     * 图表名称
     */
    @NotBlank(message = "图表名称不能为空")
    private String name;

    /**
     * 分析目标
     */
    @NotBlank(message = "分析目标不能为空")
    private String goal;

    /**
     * 图标类型
     */
    @NotBlank(message = "图标类型不能为空")
    private String chartType;

    private static final long serialVersionUID = 1L;
}
