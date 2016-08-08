package com.ailaji.manage.dao;

import java.util.List;

import com.ailaji.manage.entity.PageResult;
import com.ailaji.manage.entity.User;
import com.ailaji.manage.entity.UserResource;

public interface UserDao {

    public void addUser(User user);

    public User getUserById(String id);

    public PageResult<User> queryUserList(String sex, Integer age, Integer page, Integer pageSize);

    public List<UserResource> queryUserResourceList(Integer type, Integer tag);

}
