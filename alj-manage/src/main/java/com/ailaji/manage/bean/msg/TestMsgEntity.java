package com.ailaji.manage.bean.msg;

import com.ailaji.manage.entity.MessageEntity;

public class TestMsgEntity extends MessageEntity{
    
    private String name;
    
    private String addr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    
    

}
