package com.perfect.task.job.execute;

import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.bean.pojo.quartz.SchedulerPoJo;
import com.perfect.common.exception.job.TaskException;
import com.perfect.core.service.quartz.ISJobService;
import com.perfect.quartz.scheduler.common.SchedulerService;
import com.perfect.quartz.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 处理租户定时任务
 *
 * @author zxh
 * @date 2019年 10月20日 17:12:23
 */
@Component
@Slf4j
public class TentantJobExecute {

    @Autowired
    private ISJobService service;

    @Qualifier("perfectScheduler")
    @Autowired
    private Scheduler scheduler;

    public void execute(SJobEntity job) throws TaskException, SchedulerException {
        if(job.getId() == null){
            // 先插入数据库，获取id
            service.insert(job);
        } else {
            // 更新数据库，获取id
            service.update(job);
        }
        if(job.getIs_cron()){
            // 如果是cron表达式方式
            executeCronTrigger(job);
        } else {
            // 调用的是simpletrigger方式
            executeSimpleTrigger(job);
        }
    }

    /**
     * 执行一个用于触发的时间
     * @param job
     * @throws TaskException
     * @throws SchedulerException
     */
    public void executeSimpleTrigger (SJobEntity job) throws TaskException, SchedulerException {
        ScheduleUtils.createScheduleJobSimpleTrigger(scheduler, job);
    }

    /**
     * 执行一个用于触发的时间
     * @param job
     * @throws TaskException
     * @throws SchedulerException
     */
    public void executeCronTrigger (SJobEntity job) throws TaskException, SchedulerException {
        ScheduleUtils.createScheduleJobCron(scheduler, job);
    }


}
