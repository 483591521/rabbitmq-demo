package com.dragon.producer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import javax.print.DocFlavor;

public class PubSubModule {

    // 交换机名称
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    // 队列名称
    public static final String FANOUT_QUEUE_1 = "fanout_queue_1";
    public static final String FANOUT_QUEUE_2 = "fanout_queue_2";

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();

        // 创建频道
        Channel channel = connection.createChannel();

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
        channel.queueDeclare(FANOUT_QUEUE_2, true, false, false, null);

        // 队列绑定交换机
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHANGE, "");
        channel.queueBind(FANOUT_QUEUE_2, FANOUT_EXCHANGE, "");

        for (int i=1;i<=10;i++) {
            // 发送消息
            String message = "hello rabbit pub and sub " + i;
            /**
             * 参数
             * 1. 交换机名称，如果没有指定则使用默认的Default Exchange
             * 2. 路由key，简单模式可以传递队列名称
             * 3. 消息其他属性
             * 4. 消息内容
             */
            channel.basicPublish(FANOUT_EXCHANGE, "", null, message.getBytes());
            System.out.println("已发送消息: " + message);
        }
        // 关闭资源
        channel.close();
        connection.close();
    }
}
