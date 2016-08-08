package com.ailaji.manage.test;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * quartz一系列操作 (暂停,恢复,修改表达式)
 * 
 * @author Administrator
 *
 */
@RequestMapping("ailaji/test")
@Controller
public class JobOperateApi {

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "pauseJob", method = RequestMethod.GET)
    public void pauseJob() {
        try {
            System.out.println("任务暂停了...................................");
            JobKey jobKey = JobKey.jobKey("TestTimesPrintTask", "DEFAULT");
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "resumeJob", method = RequestMethod.GET)
    public void resumeJob() {
        try {
            System.out.println("任务恢复了...................................");
            JobKey jobKey = JobKey.jobKey("TestTimesPrintTask", "DEFAULT");
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
