package com.perfect.task.job;

import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.bean.entity.sys.config.tenant.STenantEntity;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.service.sys.config.tenant.ITenantService;
import com.perfect.quartz.util.AbstractQuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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
public class TenantEnableJob extends AbstractQuartzJob {

    @Autowired
    private ITenantService service;

    @Override
    protected void doExecute(JobExecutionContext context, SJobEntity sysJob) throws Exception {
        log.debug("TenantEnableJob");
        STenantEntity entity = BeanUtilsSupport.getBean(ITenantService.class).getById(sysJob.getJob_serial_id());
        // 写入数据库当中
        BeanUtilsSupport.getBean(ITenantService.class).enableProcess(entity);
    }
}
