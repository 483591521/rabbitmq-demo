package com.dragon.consumer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.UnsupportedEncodingException;

public class TopicModule1 {

    // 交换机名称
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    // 队列名称
    public static final String TOPIC_QUEUE_1 = "topic_queue_1";
    public static final String TOPIC_QUEUE_2 = "topic_queue_2";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

        channel.queueDeclare(TOPIC_QUEUE_1, true, false, false, null);

        channel.queueBind(TOPIC_QUEUE_1, TOPIC_EXCHANGE, "item.update");
        channel.queueBind(TOPIC_QUEUE_1, TOPIC_EXCHANGE, "item.delete");

        // 创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException, UnsupportedEncodingException {
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

        channel.basicConsume(TOPIC_QUEUE_1, true, consumer);
    }
}
