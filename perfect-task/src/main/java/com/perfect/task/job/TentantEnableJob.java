package com.perfect.task.job;

import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.bean.entity.sys.config.tenant.STentantEntity;
import com.perfect.common.exception.job.TaskException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.service.quartz.ISJobLogService;
import com.perfect.core.service.quartz.ISJobService;
import com.perfect.core.service.sys.config.tentant.ITentantService;
import com.perfect.quartz.util.AbstractQuartzJob;
import com.perfect.quartz.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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
public class TentantEnableJob extends AbstractQuartzJob {

    @Autowired
    private ITentantService service;

    @Override
    protected void doExecute(JobExecutionContext context, SJobEntity sysJob) throws Exception {
        log.debug("TentantEnableJob");
        STentantEntity entity = BeanUtilsSupport.getBean(ITentantService.class).getById(sysJob.getJob_serial_id());
        // 写入数据库当中
        BeanUtilsSupport.getBean(ITentantService.class).enableProcess(entity);
    }
}
