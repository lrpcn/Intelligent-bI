package com.lrpcn.quickdev.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.constant.StatusEnum;
import com.lrpcn.quickdev.exception.ThrowUtil;
import com.lrpcn.quickdev.manager.AiManager;
import com.lrpcn.quickdev.mapper.ChartMapper;
import com.lrpcn.quickdev.model.domain.Chart;
import com.lrpcn.quickdev.model.dto.chart.ChartQueryRequest;
import com.lrpcn.quickdev.model.dto.chart.GenChartByAiRequest;
import com.lrpcn.quickdev.model.vo.BiResponse;
import com.lrpcn.quickdev.mq.BiMessageProducer;
import com.lrpcn.quickdev.service.ChartService;
import com.lrpcn.quickdev.utils.ExcelUtils;
import com.lrpcn.quickdev.utils.SqlUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.lrpcn.quickdev.common.PageRequest.SORT_ORDER_ASC;

/**
 * chart
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements ChartService {

    @Resource
    private AiManager aiManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private BiMessageProducer biMessageProducer;

    /**
     * 构建分页查询
     *
     * @param queryRequest 查询常数
     * @return QueryWrapper
     */
    @Override
    public Wrapper<Chart> getQueryWrapper(ChartQueryRequest queryRequest) {
        QueryWrapper<Chart> wrapper = new QueryWrapper<>();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();
        if (queryRequest.getSortField() != null) {
            wrapper.like("name", queryRequest.getName());
        }
        wrapper.orderBy(SqlUtil.validSortField(sortField), StrUtil.equals(sortOrder, SORT_ORDER_ASC), sortField);
        return wrapper;
    }

    final long ONE_MB = 1024 * 1024L;

    final String ERROR = "服务错误";

    /**
     * 同步调用AI
     * todo ai生成的 json 图表有很大概率出现问题
     *
     * @param multipartFile 图表数据
     * @param request
     * @return
     */
    @Override
    public BiResponse genChartByAi(MultipartFile multipartFile, GenChartByAiRequest request) {
        String goal = request.getGoal();
        String chartType = request.getChartType();
//         分析 xlsx 文件 xlx 压缩成 csv
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        String genChart = ERROR;
        String genResult = ERROR;
        while (genResult.equals(ERROR) || !isJsonString(genChart)) {
            String result = aiManager.doChat(goal, csvData, chartType);
            if (result.contains("：") && result.contains("然后输出【【【【【"))
                genResult = result.substring(result.indexOf("：") + 1, result.indexOf("然后输出【【【【【"));
            String[] split = result.split("```json");
            if (split.length == 2) {
                genChart = split[1].substring(0, split[1].indexOf("```"));
            }
        }
        Chart chart = new Chart();
        chart.setName(request.getName());
        chart.setGoal(request.getGoal());
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setStatus(StatusEnum.SUCCEED.getValue());
        chart.setUserId(StpUtil.getLoginIdAsLong());
        boolean saveResult = save(chart);
        ThrowUtil.throwIfCustomException(!saveResult, ErrorCodeEnum.SYSTEM_ERROR, "图表保存失败");
        // 封装返回结果
        return new BiResponse(chart.getId(), genChart, genResult);
    }


    /**
     * 异步调用AI
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @Override
    public BiResponse genChartByAiAsync(MultipartFile multipartFile, GenChartByAiRequest request) {
        // 校验参数
        ThrowUtil.throwIfCustomException(ObjUtil.isNull(multipartFile), ErrorCodeEnum.PARAMETER_ERROR);
        long size = multipartFile.getSize();
        ThrowUtil.throwIfCustomException(size > ONE_MB, ErrorCodeEnum.PARAMETER_ERROR, "文件大小不能超过1MB");
        String goal = request.getGoal();
        String chartType = request.getChartType();
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        // 保存图标信息
        Chart chart = new Chart();
        chart.setName(request.getName());
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus(StatusEnum.WAIT.getValue());
        chart.setUserId(StpUtil.getLoginIdAsLong());
        boolean saveResult = save(chart);
        ThrowUtil.throwIfCustomException(!saveResult, ErrorCodeEnum.SYSTEM_ERROR, "图标保存异常");
        //  异步调用ai
        CompletableFuture.runAsync(() -> {
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus(StatusEnum.RUNNING.getValue());
            updateById(updateChart);
            String genChart = ERROR;
            String genResult = ERROR;
            while (genResult.equals(ERROR) || !isJsonString(genChart)) {
                String result = aiManager.doChat(goal, csvData, chartType);
                if (result.contains("：") && result.contains("然后输出【【【【【"))
                    genResult = result.substring(result.indexOf("：") + 1, result.indexOf("然后输出【【【【【"));
                String[] split = result.split("```json");
                if (split.length == 2) {
                    genChart = split[1].substring(0, split[1].indexOf("```"));
                }
            }
            // 更新
            updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus(StatusEnum.SUCCEED.getValue());
            updateChart.setGenChart(genChart);
            updateChart.setGenResult(genResult);
            updateById(updateChart);
        }, threadPoolExecutor);
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return biResponse;
    }

    /**
     * 异步调用AI并使用mq
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @Override
    public BiResponse genChartByAiAsyncMq(MultipartFile multipartFile, GenChartByAiRequest request) {
        // 校验参数
        ThrowUtil.throwIfCustomException(ObjUtil.isNull(multipartFile), ErrorCodeEnum.PARAMETER_ERROR);
        long size = multipartFile.getSize();
        ThrowUtil.throwIfCustomException(size > ONE_MB, ErrorCodeEnum.PARAMETER_ERROR, "文件大小不能超过1MB");
        // 从文件中获取数据  xlx 压缩成 csv
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        // 保存图标信息
        Chart chart = new Chart();
        chart.setName(request.getName());
        chart.setGoal(request.getGoal());
        chart.setChartData(csvData);
        chart.setChartType(request.getChartType());
        chart.setStatus(StatusEnum.WAIT.getValue());
        chart.setUserId(StpUtil.getLoginIdAsLong());
        boolean saveResult = save(chart);
        ThrowUtil.throwIfCustomException(!saveResult, ErrorCodeEnum.SYSTEM_ERROR, "图标保存异常");
        //  异步调用ai
        biMessageProducer.sendMessage(String.valueOf(chart.getId()));
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return biResponse;
    }

    public boolean isJsonString(String str) {
        try {
            JSON.parseObject(str);
            // 或者针对数组 JSON.parseArray(str);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * 处理图表更新失败
     *
     * @param id
     * @param execMessage
     */
    private void handleChartUpdateError(long id, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(id);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);
        boolean b = updateById(updateChartResult);
        //todo 失败应该加入队列重试
        if (!b) log.error("更新图表失败" + id + "," + execMessage);
    }
}




