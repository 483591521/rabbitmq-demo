package com.dragon.consumer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class WorkQueuesModule1 {

    public static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        // 声明(创建)队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 一次只能接收一个消息
        channel.basicQos(1);
        // 创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             *
             * @param consumerTag 消费者标签，在channel.basicConsumer时候可以指定
             * @param envelope 消息包内容，可以从中获取消息id,消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * @param properties 属性信息
             * @param body 消息
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
                    Thread.sleep(1000);

                    //确认消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 监听消息
        /**
         * 参数
         * 1. 队列名称
         * 2. 是否自动确认，设置为true表示消息接收到自动向mq回复接收到了，mq接收到消息会删除消息，设置为false则需要手动确认
         * 3. 消息接收后的回调
         */
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
