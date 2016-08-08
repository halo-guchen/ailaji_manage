package com.ailaji.manage.msg.sender;

import com.ailaji.manage.entity.MessageEntity;


public interface MessageSender {

	public void send(MessageEntity scMessageEntity) throws Exception;

	public void send(MessageEntity scMessageEntity, boolean deliverMode) throws Exception;

	public void sendWithRoutingKey(String routingKey, MessageEntity scMessageEntity) throws Exception;

	public void sendWithRoutingKey(String routingKey, MessageEntity scMessageEntity, boolean deliverMode)
			throws Exception;

}
