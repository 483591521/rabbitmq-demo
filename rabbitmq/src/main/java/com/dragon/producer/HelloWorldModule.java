package com.dragon.producer;

import com.dragon.rabbit.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class HelloWorldModule {

    public static final String QUEUE_NAME= "simple_queue";

    public static void main(String[] args) throws Exception {
        // 获取连接对象
        Connection connection = ConnectionUtil.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();
        // 声明(创建队列)
        /**
         * 参数
         * 1. 队列名称
         * 2. 是否定义持久化队列
         * 3. 是否独占本次连接
         * 4. 是否在不使用的时候自动删除队列
         * 5. 队列其他参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 发送消息
        /**
         * 参数
         * 1. 交换机名称，如果没有指定则使用默认的Default Exchange
         * 2. 路由key，简单模式可以传递队列名称
         * 3. 消息其他属性
         * 4. 消息内容
         */
        String body = "hello rabbit";
        channel.basicPublish("", QUEUE_NAME, null, body.getBytes());
        System.out.println("已发送消息: " + body);

        // 关闭资源
        channel.close();
        connection.close();
    }
}
