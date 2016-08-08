package com.ailaji.manage.msg.listener.impl;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

import com.ailaji.manage.entity.MessageEntity;
import com.ailaji.manage.msg.processor.MessageProcessor;

public class RabbitMQMessageListenerImpl implements MessageListener{

	private final static Logger logger =Logger.getLogger(RabbitMQMessageListenerImpl.class);
	
	@Override
	public void onMessage(Message message) {
		try {
			Object scMessage= messageConverter.fromMessage(message);
			try {
				logger.info(String.format("begin process [message:%s] ", new String(message.getBody())));
			} catch (Exception ignore) {
			}
			if(scMessage instanceof MessageEntity)
			{
				MessageEntity scMessageEntity = (MessageEntity)scMessage;
				messageProcessor.process(scMessageEntity);
			}
			else if(scMessage instanceof String)
			{
				messageProcessor.process((String)scMessage);
			}
			else
			{
				logger.error("not support message handle");
			}
			try {
				logger.info(String.format("end process [message:%s] ", new String(message.getBody())));
			} catch (Exception ignore) {
			}
		} catch (RuntimeException e) {
			logger.error("process message failed", e);
		}
	}

	
	private MessageProcessor messageProcessor;
	
	private MessageConverter messageConverter;

	
	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}
}
