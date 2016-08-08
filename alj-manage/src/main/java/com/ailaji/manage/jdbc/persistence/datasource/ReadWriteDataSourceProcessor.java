package com.ailaji.manage.jdbc.persistence.datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.NestedRuntimeException;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

public class ReadWriteDataSourceProcessor implements BeanPostProcessor {
    private static final Logger log = Logger.getLogger(ReadWriteDataSourceProcessor.class);
    
    private boolean forceChoiceReadWhenWrite = false;
    
    private Map<String, Boolean> readMethodMap = new HashMap<String, Boolean>();

    /**
     * 当之前操作是写的时候，是否强制从从库读
     * 默认（false） 当之前操作是写，默认强制从写库读
     * @param forceReadOnWrite
     */
    
    public void setForceChoiceReadWhenWrite(boolean forceChoiceReadWhenWrite) {
        
        this.forceChoiceReadWhenWrite = forceChoiceReadWhenWrite;
    }
    

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(!(bean instanceof NameMatchTransactionAttributeSource)) {
            return bean;
        }
        
        try {
            NameMatchTransactionAttributeSource transactionAttributeSource = (NameMatchTransactionAttributeSource)bean;
            Field nameMapField = ReflectionUtils.findField(NameMatchTransactionAttributeSource.class, "nameMap");
            nameMapField.setAccessible(true);
            Map<String, TransactionAttribute> nameMap = (Map<String, TransactionAttribute>) nameMapField.get(transactionAttributeSource);
            
            for(Entry<String, TransactionAttribute> entry : nameMap.entrySet()) {
                RuleBasedTransactionAttribute attr = (RuleBasedTransactionAttribute)entry.getValue();

                //仅对read-only的处理
                if(!attr.isReadOnly()) {
                    continue;
                }
                
                String methodName = entry.getKey();
                Boolean isForceChoiceRead = Boolean.FALSE;
                if(forceChoiceReadWhenWrite) {
                    //不管之前操作是写，默认强制从读库读 （设置为NOT_SUPPORTED即可）
                    //NOT_SUPPORTED会挂起之前的事务
                    attr.setPropagationBehavior(Propagation.NOT_SUPPORTED.value());
                    isForceChoiceRead = Boolean.TRUE;
                } else {
                    //否则 设置为SUPPORTS（这样可以参与到写事务）
                    attr.setPropagationBehavior(Propagation.SUPPORTS.value());
                }
                log.debug(String.format("read/write transaction process  method:{%s} force read:{%s}", methodName, isForceChoiceRead));
                readMethodMap.put(methodName, isForceChoiceRead);
            }
            
        } catch (Exception e) {
            throw new ReadWriteDataSourceTransactionException("process read/write transaction error", e);
        }
        
        return bean;
    }
    
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private class ReadWriteDataSourceTransactionException extends NestedRuntimeException {
        /**
		 * 
		 */
		private static final long serialVersionUID = 4590520846815238513L;

		public ReadWriteDataSourceTransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    
    
    
    
    public Object determineReadOrWriteDB(ProceedingJoinPoint pjp) throws Throwable {
        
        if (isChoiceReadDB(pjp.getSignature().getName())) {
            ReadWriteDataSourceDecision.markRead();
        } else {
            ReadWriteDataSourceDecision.markWrite();
        }
            
        try {
            return pjp.proceed();
        } finally {
            ReadWriteDataSourceDecision.reset();
        }
        
        
    }
    
    private boolean isChoiceReadDB(String methodName) {

        String bestNameMatch = null;
        for (String mappedName : this.readMethodMap.keySet()) {
            if (isMatch(methodName, mappedName)) {
                bestNameMatch = mappedName;
                break;
            }
        }

        Boolean isForceChoiceRead = readMethodMap.get(bestNameMatch);
        //表示强制选择 读 库
        if(isForceChoiceRead == Boolean.TRUE) {
            return true;
        }
        
        //如果之前选择了写库 现在还选择 写库
        if(ReadWriteDataSourceDecision.isChoiceWrite()) {
            return false;
        }
        
        //表示应该选择读库
        if(isForceChoiceRead != null) {
            return true;
        }
        //默认选择 写库
        return false;
    }


    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

}