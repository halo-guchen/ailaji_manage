package com.ailaji.manage.msg.processor.impl;

import com.ailaji.manage.bean.msg.TestMsgEntity;
import com.ailaji.manage.entity.MessageEntity;
import com.ailaji.manage.msg.processor.MessageProcessor;

public class TestRabbitMessageProcessorImpl implements MessageProcessor {

    @Override
    public void process(MessageEntity messageEntities) {
        if (messageEntities == null) {
            return;
        }

        if (messageEntities instanceof TestMsgEntity) {
            try {
                TestMsgEntity test = (TestMsgEntity) messageEntities;
                System.out.println("接收到的消息为=" + test.getName());
            } catch (Exception ex) {
                //logger.error("process business order trace exception", ex);
            }

        }

    }

    @Override
    public void process(String message) {
        // TODO Auto-generated method stub

    }

}
