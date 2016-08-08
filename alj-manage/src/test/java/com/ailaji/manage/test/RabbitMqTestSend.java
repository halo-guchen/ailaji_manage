package com.ailaji.manage.test;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqTestSend {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        /**
         * 创建连接连接到MabbitMQ
         */
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            // 创建一个连接
            Connection connection = factory.newConnection();
            // 创建一个频道
            Channel channel = connection.createChannel();
            // 指定一个队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发送的消息
            for (int i = 0; i < 10; i++)  
            {  
                String dots = "";  
                for (int j = 0; j <= i; j++)  
                {  
                    dots += ".";  
                }  
                String message = "helloworld" + dots+dots.length();  
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());  
                System.out.println(" [x] Sent '" + message + "'");  
            } 
            // 关闭频道和连接
            channel.close();
            connection.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
