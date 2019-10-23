package com.perfect.task.job;

import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.core.service.quartz.ISJobService;
import com.perfect.quartz.util.AbstractQuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理租户定时任务
 *
 * @author zxh
 * @date 2019年 10月20日 17:12:23
 */
@Component
@Slf4j
public class TentantDisableJob extends AbstractQuartzJob {

    @Autowired
    private ISJobService service;

    @Override
    protected void doExecute(JobExecutionContext context, SJobEntity sysJob) throws Exception {
        log.debug("TentantDisableJob");
    }
}
