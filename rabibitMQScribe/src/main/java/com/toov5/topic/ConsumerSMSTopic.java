package com.toov5.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.toov5.utils.MQConnectionUtils;

//邮件消费者
public class ConsumerSMSTopic {
	private static final String SMS_QUEUE ="sms_queue_topic";
	//交换机名称
   private static final String EXCHANGE_NAME = "my_topic"; 
     public static void main(String[] args) throws IOException, TimeoutException {
    	 System.out.println("短信消费者启动");
    	//建立MQ连接
		 Connection connection = MQConnectionUtils.newConnection(); 
		//创建通道
	 	 Channel channel = connection.createChannel();
	 	 
	    //消费者声明队列
	 	 channel.queueDeclare(SMS_QUEUE, false, false, false, null);
	 	//消费者队列绑定 路由
	 	channel.queueBind(SMS_QUEUE, EXCHANGE_NAME, "log.#");
	 	 //消费者监听消息
	  DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
	 		 //重写监听方法
	 		@Override
	 		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
	 				throws IOException { 			
	 			String msg = new String(body,"UTF-8");
	 			System.out.println("短信消费者获取生产者消息"+msg);
	 		}
	 	};
	 	channel.basicConsume(SMS_QUEUE,true, defaultConsumer);   //绑定队列 事件监听
	 	   
	}
}
