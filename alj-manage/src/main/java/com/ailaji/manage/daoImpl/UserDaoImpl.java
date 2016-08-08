package com.ailaji.manage.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ailaji.manage.dao.UserDao;
import com.ailaji.manage.entity.PageResult;
import com.ailaji.manage.entity.User;
import com.ailaji.manage.entity.UserResource;
import com.ailaji.manage.jdbc.persistence.Criteria;
import com.ailaji.manage.jdbc.persistence.JdbcDaoImpl;
import com.ailaji.manage.utils.NumberHelper;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

@Component
public class UserDaoImpl extends JdbcDaoImpl implements UserDao {

    @Override
    public void addUser(User user) {
        save(user);
    }

    @Override
    public User getUserById(String id) {
        Criteria c = Criteria.create(User.class);
        c.where(User.ID, new String[] { id });
        // c.include(User.ACCOUNT);加入白名单,即查出的数据只是account
        return querySingleResult(c);
    }

    @Override
    public PageResult<User> queryUserList(String sex, Integer age, Integer page, Integer pageSize) {
        Criteria c = Criteria.create(User.class);
        c.where("1", new String[] { "1" });
        if (StringUtils.isNotEmpty(sex))
            c.and(User.SEX, new String[] { sex });
        if (NumberHelper.validatePositive(age))
            c.and(User.AGE, new Integer[] { age });
        int count = queryCount(c);
        List<User> pageList = queryListLimit(c, (page - 1) * pageSize, pageSize);
        return new PageResult<User>(pageList, page, pageSize, count);
    }

    @Override
    public List<UserResource> queryUserResourceList(Integer type, Integer tag) {
        Criteria c = Criteria.create(UserResource.class);
        c.where("1", new String[] { "1" });
        if (NumberHelper.validatePositive(type))
            c.and(UserResource.TYPE, new Integer[] { type });

        List<UserResource> resourcelist = queryList(c);
        if (NumberHelper.validatePositive(tag)) {
            List<UserResource> filterUserResourceList = new ArrayList<UserResource>();
            for (UserResource userResource : resourcelist) {
                String[] tags = userResource.getTag().split(",");
                boolean flag = ArrayUtils.contains(tags, new String[] { String.valueOf(tag) });
                if (flag) {
                    filterUserResourceList.add(userResource);
                }
            }
            return filterUserResourceList;
        }
        return resourcelist;
    }

}
