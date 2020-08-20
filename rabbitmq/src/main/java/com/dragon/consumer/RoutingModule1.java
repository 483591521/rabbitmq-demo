package com.dragon.consumer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.UnsupportedEncodingException;

public class RoutingModule1 {

    // 交换机名称
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    // 队列名称
    public static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    public static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(DIRECT_QUEUE_INSERT, true, false, false, null);

        channel.queueBind(DIRECT_QUEUE_INSERT, DIRECT_EXCHANGE, "insert");

        // 创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
            }
        };

        channel.basicConsume(DIRECT_QUEUE_INSERT, true, consumer);
    }
}
