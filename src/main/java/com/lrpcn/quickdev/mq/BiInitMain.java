package com.lrpcn.quickdev.mq;

import com.lrpcn.quickdev.constant.BiMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 功能: 初始化mq
 * 作者: lrpcn
 * 日期: 2024/2/13 16:35
 */
public class BiInitMain {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            // 创建连接
            Connection connection = factory.newConnection();
            // 创建通道
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = BiMqConstant.EXCHANGE;
            // 声明交换机，指定类型为direct
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
