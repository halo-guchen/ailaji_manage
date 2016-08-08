package com.ailaji.manage.msg.processor;

import com.ailaji.manage.entity.MessageEntity;

public interface MessageProcessor {
	public void process(MessageEntity messageEntities);
	public void process(String message);
}
