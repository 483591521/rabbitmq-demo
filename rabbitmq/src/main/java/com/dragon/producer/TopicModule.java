package com.dragon.producer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TopicModule {

    // 交换机名称
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    // 队列名称
    public static final String TOPIC_QUEUE_1 = "topic_queue_1";
    public static final String TOPIC_QUEUE_2 = "topic_queue_2";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

        String message = "新增了商品。topic模式。 routing key : item.insert";
        channel.basicPublish(TOPIC_EXCHANGE, "item.insert", null, message.getBytes());
        System.out.println("已经发送消息: " + message);

        message = "删除了商品。topic模式。 routing key : item.delete";
        channel.basicPublish(TOPIC_EXCHANGE, "item.delete", null, message.getBytes());
        System.out.println("已经发送消息: " + message);

        // 关闭资源
        channel.close();
        connection.close();
    }
}
