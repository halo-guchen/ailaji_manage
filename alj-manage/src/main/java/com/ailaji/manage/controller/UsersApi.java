package com.ailaji.manage.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ailaji.manage.bean.user.FileInputResultBean;
import com.ailaji.manage.entity.PageResult;
import com.ailaji.manage.entity.ResponseResult;
import com.ailaji.manage.entity.User;
import com.ailaji.manage.entity.UserResource;
import com.ailaji.manage.service.UserService;
import com.ailaji.manage.task.TestRabbitMqSender;

@Controller
@RequestMapping("private/user")
public class UsersApi{

    private final static Logger logger = Logger.getLogger(UsersApi.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private TestRabbitMqSender sender;

    /** 用户信息页面 */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public String userMainPage() {
        return "user/userInfo";
    }

    /** 用户资源文件页面 */
    @RequestMapping(value = "resource/page", method = RequestMethod.GET)
    public String resourceMainPage() {
        return "user/file_manager";
    }

    /** 查询所有用户 */
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public ResponseEntity<ResponseResult<User>> queryAllUser(@RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit) {
        ResponseResult<User> result = new ResponseResult<User>();
        try {
            // 每页行数
            PageResult<User> queryUserList = userService.queryUserList(null, null, (offset + limit) / limit,
                    limit);
            result.setRows(queryUserList.getPage_list());
            result.setTotal(queryUserList.getTotal());
        } catch (Exception e) {
            String errMsg = String.format("%s系统错误", "queryUser");
            logger.error(errMsg, e);
        }
        return ResponseEntity.ok(result);

    }

    /** 查询用户资源 */
    @RequestMapping(value = "resource/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserResource>> queryUserResource(
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "tag", required = false) Integer tag) {
        List<UserResource> UserResourceList = null;
        try {
            UserResourceList = userService.queryUserResourceList(type, tag);
        } catch (Exception e) {
            String errMsg = String.format("%s系统错误", "queryResource");
            logger.error(errMsg, e);
        }
        return ResponseEntity.ok(UserResourceList);

    }
    
    
    @RequestMapping(value="test")
    public void testRabbit(){
        sender.test();
    }

}
