package com.lrpcn.quickdev.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.lrpcn.quickdev.annotation.MyRateLimiter;
import com.lrpcn.quickdev.annotation.MyRedisLimiter;
import com.lrpcn.quickdev.common.DeleteRequest;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.common.ResultResponse;
import com.lrpcn.quickdev.constant.RedisKeyConstant;
import com.lrpcn.quickdev.exception.ThrowUtil;
import com.lrpcn.quickdev.model.domain.Chart;
import com.lrpcn.quickdev.model.dto.chart.*;
import com.lrpcn.quickdev.model.vo.BiResponse;
import com.lrpcn.quickdev.service.ChartService;
import com.lrpcn.quickdev.utils.RedisUtil;
import com.lrpcn.quickdev.utils.bean.ChartConversionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 功能: 核心业务
 * 作者: lrpcn
 * 日期: 2024/2/12 11:16
 */
@Slf4j
@Api("chart")
@RestController
@RequestMapping("/api/chart")
public class ChartController {

    private static final long ONE_MB = 1024 * 1024L;

    @Resource
    private ChartService chartService;

    @Resource
    private RedisUtil redisUtil;

    private static final Gson GSON = new Gson();

    /**
     * 同步调用ai
     *
     * @param multipartFile 数据文件
     * @param request       请求参数
     * @return
     */
    @MyRedisLimiter(key = "key:genChart", qps = 2)
    @ApiOperation("同步调用ai")
    @PostMapping("/gen")
    public ResultResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                   @Validated GenChartByAiRequest request) {
        // 校验参数
        ThrowUtil.throwIfCustomException(ObjUtil.isNull(multipartFile), ErrorCodeEnum.PARAMETER_ERROR);
        long size = multipartFile.getSize();
        ThrowUtil.throwIfCustomException(size > ONE_MB, ErrorCodeEnum.PARAMETER_ERROR, "文件大小不能超过1MB");
        BiResponse biResponse = chartService.genChartByAi(multipartFile, request);
        return ResultResponse.success(biResponse);
    }

    /**
     * 异步调用ai
     *
     * @param multipartFile 数据文件
     * @param request       请求参数
     * @return
     */
    @MyRedisLimiter(key = "key:genChart", qps = 2)
    @ApiOperation("异步调用接口")
    @PostMapping("/gen/async/1")
    public ResultResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile,
                                                        @Validated GenChartByAiRequest request) {
        BiResponse biResponse = chartService.genChartByAiAsync(multipartFile, request);
        return ResultResponse.success(biResponse);
    }

    /**
     * 异步调用ai
     *
     * @param multipartFile 数据文件
     * @param request       请求参数
     * @return
     */
    @MyRedisLimiter(key = "key:genChart:mq", qps = 2)
    @ApiOperation("异步调用接口")
    @PostMapping("/gen/async")
    public ResultResponse<BiResponse> genChartByAiAsyncMq(@RequestPart("file") MultipartFile multipartFile,
                                                          @Validated GenChartByAiRequest request) {
        BiResponse biResponse = chartService.genChartByAiAsyncMq(multipartFile, request);
        return ResultResponse.success(biResponse);
    }

    /**
     * 添加接口
     *
     * @param request 请求参数
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("添加用户")
    @PostMapping("/add")
    public ResultResponse<Long> addChart(@RequestBody @Validated ChartAddRequest request) {
        Chart chart = ChartConversionUtil.toChart(request);
        chartService.save(chart);
        return ResultResponse.success(chart.getId());
    }

    /**
     * 删除接口
     *
     * @param deleteRequest 请求参数
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("删除用户")
    @PostMapping("/delete")
    public ResultResponse<Void> delChart(@RequestBody @Validated DeleteRequest deleteRequest) {
        chartService.removeById(deleteRequest.getId());
        return ResultResponse.success();
    }

    /**
     * 更新接口
     *
     * @param request 请求参数
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("更新用户信息")
    @PostMapping("/update")
    public ResultResponse<Void> updateChart(@RequestBody @Validated ChartUpdateRequest request) {
        Chart chart = ChartConversionUtil.toChart(request);
        if (ObjUtil.isNull(chart.getUpdateTime())) {
            chart.setUpdateTime(new Date());
        }
        chartService.updateById(chart);
        return ResultResponse.success();
    }

    /**
     * 获取图表信息
     *
     * @param id 请求参数
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("获取用户信息")
    @GetMapping("/get")
    public ResultResponse<Chart> getChartInfo(long id) {
        ThrowUtil.throwIfCustomException(id <= 0, ErrorCodeEnum.PARAMETER_ERROR);
        Chart chart = chartService.getById(id);
        if (ObjUtil.isNull(chart)) {
            return ResultResponse.error(ErrorCodeEnum.NOT_FOUND_ERROR);
        }
        return ResultResponse.success(chart);
    }

    /**
     * 分页查询
     *
     * @param queryRequest 请求参数
     * @return
     */
    @SaCheckRole("admin")
    @ApiOperation("分页查询")
    @PostMapping("/list/page")
    public ResultResponse<Page<Chart>> getChartList(@RequestBody @Validated ChartQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<Chart> postPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(queryRequest));
        return ResultResponse.success(postPage);
    }

    /**
     * todo 主要接口
     * 业务主要接口
     * 分页获取当前用户创建的资源列表
     * 用了redis缓存提高性能
     * @param chartQueryRequest
     * @return
     */
    @SaCheckRole("user")
    @PostMapping("/my/list/page")
    public ResultResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        // 参数设置
        long userId = StpUtil.getLoginIdAsLong();
        chartQueryRequest.setUserId(userId);
        int current = chartQueryRequest.getCurrent();
        int size = chartQueryRequest.getPageSize();
        // 获取分页数据的id
        List<Long> chartIds = chartService.redisPage(current, size, chartQueryRequest.getName());
        List<String> ids = chartIds.stream().map(id -> RedisKeyConstant.KEY_CHART_BY_ID + id).collect(Collectors.toList());
        Map<String, String> jsonList = redisUtil.mGet(ids);
        List<Chart> records = new ArrayList<>();
        jsonList.forEach((id, json) -> {
            if (StrUtil.isBlank(json)) {
                Chart byId = chartService.getById(id);
                redisUtil.set(RedisKeyConstant.KEY_CHART_BY_ID + id, GSON.toJson(byId),
                        10 + RandomUtil.randomInt(5), TimeUnit.MINUTES);
                records.add(byId);
            } else {
                Chart chart = GSON.fromJson(json, Chart.class);
                redisUtil.renewal(RedisKeyConstant.KEY_CHART_BY_ID + id, RandomUtil.randomInt(10),
                        TimeUnit.MINUTES);
                records.add(chart);
            }
        });
        Page<Chart> chartPage = new Page<>(current, size);
        chartPage.setRecords(records);
//        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
//                chartService.getQueryWrapper(chartQueryRequest));
        return ResultResponse.success(chartPage);
    }

    /**
     * 用户编辑接口
     *
     * @param request 请求参数
     * @return
     */
    @MyRateLimiter(resourceId = "/v1/api/chart/edit", qps = 5.0)
    @SaCheckRole("chart")
    @ApiOperation("用户编辑用户信息")
    @PostMapping("/edit")
    public ResultResponse<Void> editChart(@RequestBody @Validated ChartEditRequest request) {
        Chart chart = ChartConversionUtil.toChart(request);
        chart.setId(StpUtil.getLoginIdAsLong());
        chart.setCreateTime(new Date());
        chartService.updateById(chart);
        return ResultResponse.success();
    }
}
