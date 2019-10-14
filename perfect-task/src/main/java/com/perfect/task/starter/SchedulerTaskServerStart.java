package com.perfect.task.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动通过mq过来定时任务
 *
 * @author zxh
 */
@SpringBootApplication(
    exclude = { DataSourceAutoConfiguration.class },
    scanBasePackages = {
            "com.perfect.framework",
            "com.perfect.*",
            "com.perfect.redis",
            "com.perfect.mq.rabbitmq",
            "com.perfect.quartz.config"
        })
@EnableTransactionManagement
@EntityScan(basePackages = {"com.perfect.*"})
@Slf4j
@EnableCaching
public class SchedulerTaskServerStart {
    public static void main(String[] args) {
        log.info("-----------------------启动开始-------------------------");
        SpringApplication.run(SchedulerTaskServerStart.class, args);
        log.info("-----------------------启动完毕-------------------------");
    }
}
