package com.ailaji.manage.msg.sender.impl;

import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.ailaji.manage.entity.MessageEntity;
import com.ailaji.manage.msg.sender.MessageSender;

public class RabbitMQMessageSenderImpl implements MessageSender {

	static Logger logger = Logger.getLogger(RabbitMQMessageSenderImpl.class);
	private RabbitTemplate rabbitTemplate;

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	private int retryTime = 20;
	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}

	@Override
	public void send(MessageEntity scMessageEntity) throws Exception {
		send(scMessageEntity, false);
	}

	@Override
	public void send(MessageEntity scMessageEntity, boolean deliverMode) throws Exception {
		int time = 0;
		boolean send = false;
		while (time++ <= retryTime) {
			try {
				if (deliverMode) {
					rabbitTemplate.convertAndSend(scMessageEntity, new MessagePostProcessor() {
						@Override
						public Message postProcessMessage(Message message) throws AmqpException {
							message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
							return message;
						}
					});
				} else {
					rabbitTemplate.convertAndSend(scMessageEntity);
				}
				send = true;
				break;
			} catch (Exception ex) {
				logger.info("send exception, retry", ex);
				Thread.sleep(200);
			}
			
		}
		if (!send) {
				throw new Exception("Send queue failed");
			}
	}

	@Override
	public void sendWithRoutingKey(String routingKey, MessageEntity scMessageEntity) throws Exception {
		sendWithRoutingKey(routingKey, scMessageEntity, false);
	}

	@Override
	public void sendWithRoutingKey(String routingKey, MessageEntity scMessageEntity, boolean deliverMode)
			throws Exception {
		int time = 0;
		boolean send = false;
		while (time++ <= retryTime) {
			try {
				if (deliverMode) {
					rabbitTemplate.convertAndSend(routingKey, scMessageEntity, new MessagePostProcessor() {

						public Message postProcessMessage(Message message) throws AmqpException {
							message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
							return message;
						}
					});
				} else {
					rabbitTemplate.convertAndSend(routingKey, scMessageEntity);
				}
				send = true;
				break;
			} catch (Exception ex) {
				logger.info("send to exchange exception, retry", ex);
				Thread.sleep(200);
			}
			if (!send) {
				throw new Exception("Send to exchange failed!");
			}
		}

	}
}
