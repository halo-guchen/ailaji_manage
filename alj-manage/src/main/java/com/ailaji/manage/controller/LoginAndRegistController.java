package com.ailaji.manage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ailaji.manage.service.RedisService;

/**
 * 登录注册的入口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("face")
public class LoginAndRegistController {
    
    private static String URL="http://222.73.117.158/msg/";// 应用地址失效
    private static String ACCOUNT = "jiekou-clcs-09";// 账号失效
    private static String PSWD = "Admin888";// 密码失效
    
    //@Autowired
    //private UserService userService;
    
    /*@Autowired
    private RedisService redisService;*/
    
    /*@RequestMapping("logon")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(User user,HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();
        List<User> users = this.userService.queryList(user);
        if(CollectionUtils.isNotEmpty(users)){
            return ResponseBuilder.build(WebReturnCode.TARGET_NOT_FOUND,String.format("用户%s已存在", user.getAccount()),null);
        }
        String vcode = user.getVcode();
        String code = (String) session.getAttribute("code");
        if(vcode.equalsIgnoreCase(code)){
            if(users.size()>0){
                User currentUser=users.get(0);
                session.setAttribute("currentUser", currentUser.getUsername());
                result.put("msg", "ok");
            }else{
                result.put("msg", "用户名或密码错误！");
            }
        }else{
            result.put("msg", "验证码错误！");
        }
        return ResponseEntity.ok(result);
    }*/
    
    /*@RequestMapping("regist")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> regist(User user,HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();
        String account = user.getAccount();
        String vcode = user.getVcode();
        if(null==this.userService.queryUserByAccount(user)){
            if(this.redisService.get(account).equals(vcode)){
                user.setUsername(account);
                user.setSex("男");
                user.setId(UUID.randomUUID().toString().replace("-", ""));
                this.userService.save(user);
                result.put("msg", "ok");
            }else{
                result.put("msg", "验证码错误！");
            }
        }else{
            result.put("msg", "用户名已存在！");
        }
        return ResponseEntity.ok(result);
        
    }*/
    
    /*@RequestMapping(value="messageCode",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> getMessageCode(String account,HttpSession session){
        Integer code=(int)((Math.random()*9+1)*100000);
        //String msg = "手机尾号为["+account.substring(7, 11)+"]的用户，您的验证码是"+code+"，5分钟内有效。";// 短信内容
        String msg="您好，您的验证码是"+code;// 短信内容
        this.redisService.set(account, code.toString(), 300);
        String sendMsg = sendMsg(account,msg);
        System.out.println(sendMsg);
        return ResponseEntity.ok(null);
    }*/
    
    /**
     * 发送短信
     *          
     * @return
     */
   /* public String sendMsg(String mobile, String msg){
        boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
        String product = null;// 产品ID
        String extno = null;// 扩展码
        String returnString=null;
        try {
            returnString = HttpSender.batchSend(URL, ACCOUNT, PSWD, mobile, msg, needstatus, product, extno);
            // TODO 处理返回值,参见HTTP协议文档
    } catch (Exception e) {
            // TODO 处理异常
            e.printStackTrace();
    }
        return returnString;
        
    }*/
    /**
     * 生成6位随机数
     * @param args
     */
    public static void main(String[] args) {
        String account="15605161303";
        System.out.println(account.subSequence(7, 11));
        System.out.println((int)((Math.random()*9+1)*100000)); 
    }

}
