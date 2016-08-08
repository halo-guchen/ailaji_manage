package com.ailaji.manage.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob;

import com.dubbo.api.service.DubboTestService;

public class TestTimesPrintTask extends StatefulMethodInvokingJob {

    private static DubboTestService service;

    private void setContext(JobExecutionContext context) {
        try {
            // 获取JobExecutionContext中的service对象
            SchedulerContext schCtx = context.getScheduler().getContext();
            // 获取Spring中的上下文
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContextKey");
            //appCtx.getBean("cacheDataManager");
            service=(DubboTestService) appCtx.getBean("dubboTestService");
        } catch (Exception e) {
            logger.error("setContext exception!", e);
        }

    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info(String.format("begin reload goods list..."));
            setContext(context);
            System.out.println("正在执行任务.................");
            service.sayHello("你好!");
            logger.info(String.format("end reload goods list."));
        } catch (Exception e) {
            logger.error(String.format("reload goods list exception!"), e);
        }
    }

}
