package com.dragon.rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {

    public static Connection getConnection() throws Exception {
        // 创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置地址 默认localhost
        factory.setHost("localhost");
        // 连接端口 默认5672
        factory.setPort(5672);
        // 虚拟机名称 默认 /
        factory.setVirtualHost("/dragon");
        // 用户名 默认guest
        factory.setUsername("dragon");
        // 密码 默认guest
        factory.setPassword("chen521...");
        // 返回连接对象
        return factory.newConnection();
    }
}
