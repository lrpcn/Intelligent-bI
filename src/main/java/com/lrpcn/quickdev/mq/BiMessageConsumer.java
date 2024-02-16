package com.lrpcn.quickdev.mq;

import com.lrpcn.quickdev.constant.BiMqConstant;
import com.lrpcn.quickdev.constant.StatusEnum;
import com.lrpcn.quickdev.manager.AiManager;
import com.lrpcn.quickdev.model.domain.Chart;
import com.lrpcn.quickdev.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 功能: 消费任务
 * 作者: lrpcn
 * 日期: 2024/2/13 16:17
 */
@Slf4j
@Component
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    final String ERROR = "服务错误";

    /**
     * 接受消息
     *
     * @param message     接收到的消息
     * @param channel     与消息队列交互的通道
     * @param deliveryTag 消息的交付标签
     */
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL",concurrency = "1")
    public void receiveMessage(String message,
                               Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        // 获取chart
        Chart chart = chartService.getById(message);
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(StatusEnum.RUNNING.getValue());
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            handleChartUpdateError(chart.getId(), "更新图表失败");
            return;
        }
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        // 调用ai
        String genChart = ERROR;
        String genResult = ERROR;
        while (genResult.equals(ERROR) || !chartService.isJsonString(genChart)) {
            String result = aiManager.doChat(goal, csvData, chartType);
            if (result.contains("：") && result.contains("然后输出【【【【【"))
                genResult = result.substring(result.indexOf("：") + 1, result.indexOf("然后输出【【【【【"));
            String[] split = result.split("```json");
            if (split.length == 2) {
                genChart = split[1].substring(0, split[1].indexOf("```"));
            }
        }
        updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(StatusEnum.SUCCEED.getValue());
        updateChart.setGenChart(genChart);
        updateChart.setGenResult(genResult);
        b = chartService.updateById(updateChart);
        if (!b) {
            handleChartUpdateError(chart.getId(), "更新图表失败");
        }
        log.info("接收到信息：{}", message);
        // 确实消息
        channel.basicAck(deliveryTag, false);
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
        boolean b = chartService.updateById(updateChartResult);
        //todo 失败应该加入队列重试
        if (!b) log.error("更新图表失败" + id + "," + execMessage);
    }

}
