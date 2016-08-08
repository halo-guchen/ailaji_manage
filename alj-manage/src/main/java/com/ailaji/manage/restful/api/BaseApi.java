package com.ailaji.manage.restful.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.ailaji.manage.utils.JsonUtil;
import com.ailaji.manage.utils.ReflectUtil;

public class BaseApi {
    
    private final static Logger logger = Logger.getLogger(BaseApi.class);
    
    public void logParam(JoinPoint j){
        StringBuilder s = new StringBuilder();
        try {
                Object[] args = j.getArgs();
                Class<?> targetClazz = j.getTarget().getClass();
                String className = targetClazz.getSimpleName();
                Signature signature = j.getSignature();
                MethodSignature methodSignature = (MethodSignature) signature;
                String[] methodParamNames = methodSignature.getParameterNames();
                String methodName = signature.getName();
                //String[] methodParamNames = ReflectUtil.getMethodParamNames(targetClazz, methodName);
                if (null != args && args.length > 0) {
                        s.append(className).append("@").append(methodName).append(" parameters-> ");
                        for (int i = 0; i < args.length; i++) {
                                Object o = args[i];
                                if (o instanceof HttpServletRequest || o instanceof HttpServletResponse || o instanceof HttpSession) {
                                        continue;
                                }
                                s.append(methodParamNames[i]).append(":").append(JsonUtil.getJsonFromObject(o)).append(" ");
                        }
                        logger.info(s.toString());
                }
        } catch (Throwable t) {
            logger.error(s.toString(), t);
        }
        
    }

}
