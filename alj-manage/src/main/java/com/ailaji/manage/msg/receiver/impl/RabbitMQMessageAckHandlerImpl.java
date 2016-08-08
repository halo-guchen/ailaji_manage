package com.ailaji.manage.msg.receiver.impl;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.transaction.PlatformTransactionManager;

import com.ailaji.manage.msg.listener.impl.RabbitMQMessageAckListenerImpl;
import com.ailaji.manage.msg.processor.MessageProcessor;
import com.ailaji.manage.msg.receiver.MessageHandler;

public class RabbitMQMessageAckHandlerImpl implements MessageHandler{

private final static Logger logger = Logger.getLogger(RabbitMQMessageAckHandlerImpl.class);
	
	private SimpleMessageListenerContainer   container;
	
	public RabbitMQMessageAckHandlerImpl(String queueName, ConnectionFactory connectionFactory, 
			MessageProcessor messageProcessor, MessageConverter messageConverter) {
        this.container = new SimpleMessageListenerContainer();
        container.setQueueNames(queueName);
        container.setConnectionFactory(connectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        RabbitMQMessageAckListenerImpl listener = new RabbitMQMessageAckListenerImpl();
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
