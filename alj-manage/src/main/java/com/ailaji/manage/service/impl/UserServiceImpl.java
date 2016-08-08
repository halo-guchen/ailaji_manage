package com.ailaji.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ailaji.manage.dao.UserDao;
import com.ailaji.manage.entity.PageResult;
import com.ailaji.manage.entity.User;
import com.ailaji.manage.entity.UserResource;
import com.ailaji.manage.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public PageResult<User> queryUserList(String sex, Integer age, Integer page, Integer pageSize) {
        return userDao.queryUserList(sex, age, page, pageSize);
    }

    @Override
    public List<UserResource> queryUserResourceList(Integer type, Integer tag) {
        return userDao.queryUserResourceList(type, tag);
    }

}
