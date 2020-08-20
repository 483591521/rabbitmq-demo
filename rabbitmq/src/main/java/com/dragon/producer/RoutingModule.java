package com.dragon.producer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RoutingModule {

    // 交换机名称
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    // 队列名称
    public static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    public static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

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
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明(创建)队列
        /**
         * 参数
         * 1. 队列名称
         * 2. 是否定义持久化队列
         * 3. 是否独占本次连接
         * 4. 是否在不使用的时候自动删除队列
         * 5. 队列其他参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT, true, false, false, null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE, true, false, false, null);

        // 队列绑定交换机
        channel.queueBind(DIRECT_QUEUE_INSERT, DIRECT_EXCHANGE, "insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE, DIRECT_EXCHANGE, "update");

        // 发送消息
        String message = "新增了商品。路由模式; routing key 为 insert";
        /**
         * 参数
         * 1. 交换机名称，如果没有指定则使用默认的Default Exchange
         * 2. 路由key，简单模式可以传递队列名称
         * 3. 消息其他属性
         * 4. 消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "insert", null, message.getBytes());
        System.out.println("已发送消息: " + message);

        // 发送消息
        message = "修改了商品。路由模式; routing key 为 update";
        /**
         * 参数
         * 1. 交换机名称，如果没有指定则使用默认的Default Exchange
         * 2. 路由key，简单模式可以传递队列名称
         * 3. 消息其他属性
         * 4. 消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "update", null, message.getBytes());
        System.out.println("已发送消息: " + message);

        // 关闭资源
        channel.close();
        connection.close();
    }
}
