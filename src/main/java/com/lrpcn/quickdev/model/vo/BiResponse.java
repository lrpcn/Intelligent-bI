package com.lrpcn.quickdev.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/12 17:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiResponse {

    private Long chartId;

    private String genChart;

    private String genResult;

}
