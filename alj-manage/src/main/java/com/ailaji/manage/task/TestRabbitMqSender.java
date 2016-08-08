package com.ailaji.manage.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ailaji.manage.bean.msg.TestMsgEntity;
import com.ailaji.manage.msg.sender.MessageSender;

@Component
public class TestRabbitMqSender {

    @Autowired
    private MessageSender TestSender;

    public void test() {
        try {
            TestMsgEntity t = new TestMsgEntity();
            t.setAddr("上海");
            t.setName("嘿嘿");
            TestSender.send(t);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
