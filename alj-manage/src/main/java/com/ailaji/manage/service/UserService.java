package com.ailaji.manage.service;

import java.util.List;

import com.ailaji.manage.entity.PageResult;
import com.ailaji.manage.entity.User;
import com.ailaji.manage.entity.UserResource;

public interface UserService {

    PageResult<User> queryUserList(String sex, Integer age, Integer page, Integer pageSize);

    List<UserResource> queryUserResourceList(Integer type, Integer tag);

}
