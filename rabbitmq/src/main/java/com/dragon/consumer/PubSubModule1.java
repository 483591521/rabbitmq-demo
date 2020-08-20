package com.dragon.consumer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class PubSubModule1 {

    // 交换机名称
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    // 队列名称
    public static final String FANOUT_QUEUE_1 = "fanout_queue_1";

    public static void main(String[] args) throws Exception {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();

        // 创建频道
        Channel channel = connection.createChannel();

        // 声明交换机
        /**
         * 声明交换机
         * 1. 交换机名称
         * 2. 交换机类型: fanout topic direct headers
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 声明(创建)队列
        /**
         * 参数
         * 1. 队列名称
         * 2. 是否定义持久化队列
         * 3. 是否独占本次连接
         * 4. 是否在不使用的时候自动删除队列
         * 5. 队列其他参数
         */
        channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, null);

        // 队列绑定交换机
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHANGE, "");

        // 创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
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

        // 监听消息
        /**
         * 参数
         * 1. 队列名称
         * 2. 是否自动确认，设置为true表示消息接收到自动向mq回复接收到了，mq接收到消息会删除消息，设置为false则需要手动确认
         * 3. 消息接收后的回调
         */
        channel.basicConsume(FANOUT_QUEUE_1, true, consumer);
    }
}
