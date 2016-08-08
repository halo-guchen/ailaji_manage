package com.ailaji.manage.service.impl;

import org.springframework.stereotype.Service;

import com.dubbo.api.service.DubboTestService;



@Service("dubboTestService")
public class DubboTestServiceImpl implements DubboTestService {

    @Override
    public String sayHello(String message) {
        return "DUBBO"+message;
    }

}
