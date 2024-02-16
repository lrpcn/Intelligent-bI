package com.lrpcn.quickdev.mq;

import com.lrpcn.quickdev.constant.BiMqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/13 16:10
 */

@Component
public class BiMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message 信息内容
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BiMqConstant.EXCHANGE, BiMqConstant.BI_ROUTING_KEY, message);
    }

}
