package com.perfect.task.job.execute;

import com.perfect.bean.pojo.quartz.SchedulerPoJo;
import com.perfect.quartz.scheduler.common.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理租户定时任务
 *
 * @author zxh
 * @date 2019年 10月20日 17:12:23
 */
@Component
public class TentantJobExecute {

    @Autowired
    SchedulerService schedulerService;

    public void execute(SchedulerPoJo schedulerPoJo){
        if(schedulerPoJo.getIsCron()){
            // 如果是cron表达式方式

        } else {
            // 调用的是simpletrigger方式
            executeSimpleTrigger(schedulerPoJo);
        }
    }


    public void executeSimpleTrigger (SchedulerPoJo schedulerPoJo) {
        /**
         * 1:查看
         */
    }
}
