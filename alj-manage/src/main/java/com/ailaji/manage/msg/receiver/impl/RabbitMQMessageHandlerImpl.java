package com.ailaji.manage.msg.receiver.impl;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.transaction.PlatformTransactionManager;

import com.ailaji.manage.msg.listener.impl.RabbitMQMessageListenerImpl;
import com.ailaji.manage.msg.processor.MessageProcessor;
import com.ailaji.manage.msg.receiver.MessageHandler;

public class RabbitMQMessageHandlerImpl implements MessageHandler{

private final static Logger logger = Logger.getLogger(RabbitMQMessageHandlerImpl.class);
	
	private SimpleMessageListenerContainer   container;
	
	public RabbitMQMessageHandlerImpl(String queueName, ConnectionFactory connectionFactory, 
			MessageProcessor messageProcessor, MessageConverter messageConverter) {
        this.container = new SimpleMessageListenerContainer();
        container.setQueueNames(queueName);
        container.setConnectionFactory(connectionFactory);
        RabbitMQMessageListenerImpl listener = new RabbitMQMessageListenerImpl();
        listener.setMessageProcessor(messageProcessor);
        listener.setMessageConverter(messageConverter);
        container.setMessageListener(listener);
    }

    public void setConcurrentConsumers(int concurrentConsumers){
        container.setConcurrentConsumers(concurrentConsumers);
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        container.setTransactionManager(transactionManager);
    }
    
	@Override
	public void start() {
		logger.info(String.format("rabbit mq receive msg start, %s", container.getQueueNames()));
		this.container.initialize();
		this.container.start();
	}

	@Override
	public void stop() {
		if(null != container){
			this.container.stop();
			logger.info("rabbit mq receive msg stoped");
		}
	}

	@Override
	public void shutdown() {
		if(null != container){
			this.container.shutdown();
			logger.info("rabbit mq receive msg shutdowned");
		}
	}
}
