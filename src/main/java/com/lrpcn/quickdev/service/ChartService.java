package com.lrpcn.quickdev.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrpcn.quickdev.model.domain.Chart;
import com.lrpcn.quickdev.model.dto.chart.ChartQueryRequest;
import com.lrpcn.quickdev.model.dto.chart.GenChartByAiRequest;
import com.lrpcn.quickdev.model.vo.BiResponse;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Administrator
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-02-12 10:52:59
*/
public interface ChartService extends IService<Chart> {

    public boolean isJsonString(String str);

    /**
     * 获取分页查询Wrapper
     * @param queryRequest
     * @return
     */
    Wrapper<Chart> getQueryWrapper(ChartQueryRequest queryRequest);

    /**
     * 调用ai生成图表
     * @param multipartFile
     * @param genChartByAiRequest
     * @return
     */
    BiResponse genChartByAi(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest);

    /**
     * 自定义线程池
     * 异步调用ai生成图表
     * @param multipartFile
     * @param request
     * @return
     */
    BiResponse genChartByAiAsync(MultipartFile multipartFile, GenChartByAiRequest request);

    /**
     * mq
     * @param multipartFile
     * @param request
     * @return
     */
    BiResponse genChartByAiAsyncMq(MultipartFile multipartFile, GenChartByAiRequest request);
}
