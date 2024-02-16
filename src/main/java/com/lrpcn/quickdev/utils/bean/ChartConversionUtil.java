package com.lrpcn.quickdev.utils.bean;

import com.lrpcn.quickdev.model.domain.Chart;
import com.lrpcn.quickdev.model.dto.chart.ChartAddRequest;
import com.lrpcn.quickdev.model.dto.chart.ChartEditRequest;
import com.lrpcn.quickdev.model.dto.chart.ChartUpdateRequest;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/12 15:55
 */
public class ChartConversionUtil {

    public static Chart toChart(ChartAddRequest request) {
        return Chart.builder()
                .name(request.getName())
                .goal(request.getGoal())
                .chartData(request.getChartData())
                .chartType(request.getChartType())
                .build();
    }

    public static Chart toChart(ChartUpdateRequest request) {
        return Chart.builder()
                .id(request.getId())
                .name(request.getName())
                .goal(request.getGoal())
                .chartData(request.getChartData())
                .chartType(request.getChartType())
                .genResult(request.getGenResult())
                .userId(request.getUserId())
                .createTime(request.getCreateTime())
                .updateTime(request.getUpdateTime())
                .isDelete(request.getIsDeleted())
                .build();
    }

    public static Chart toChart(ChartEditRequest request) {
        return Chart.builder()
                .id(request.getId())
                .name(request.getName())
                .goal(request.getGoal())
                .chartData(request.getChartData())
                .chartType(request.getChartType())
                .build();
    }
}
