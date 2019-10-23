package com.perfect.task.mq.consumer;

import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.bean.pojo.mqsender.MqSenderPojo;
import com.perfect.common.exception.job.TaskException;
import com.perfect.core.service.quartz.ISJobService;
import com.perfect.framework.utils.mq.MessageUtil;
import com.perfect.mq.rabbitmq.mqenum.MQEnum;
import com.perfect.quartz.util.ScheduleUtils;
import com.perfect.task.job.TentantEnableJob;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName: PlatformTaskReceiver
 * @Description: Rabbit mq 租户消费者
 * @Author: zxh
 * @date: 2019/10/17
 * @Version: 1.0
 */

@Component
@Slf4j
public class TentantEnableConsumer {

    @Autowired
    private ISJobService service;

    @Qualifier("perfectScheduler")
    @Autowired
    private Scheduler scheduler;

    /**
     * 配置监听的哪一个队列，同时在没有queue和exchange的情况下会去创建并建立绑定关系
     * @param messageDataObject
     * @param headers
     * @param channel
     * @throws IOException
     */
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = MQEnum.MqInfo.TentantEnableTask.queueCode, durable = "true"),
            exchange = @Exchange(name=MQEnum.MqInfo.TentantEnableTask.exchange, durable = "true", type = "topic"),
            key = MQEnum.MqInfo.TentantEnableTask.routing_key
        )
    )
    @RabbitHandler
    public void onMessage(@Payload Message messageDataObject, @Headers Map<String, Object> headers, Channel channel)
        throws IOException, TaskException, SchedulerException {
        MqSenderPojo mqSenderPojo = MessageUtil.getMessageBodyBean(messageDataObject);
        Object messageContext = MessageUtil.getMessageContextBean(messageDataObject);

        /**
         * 执行job
         */
        executeTrigger((SJobEntity)messageContext);

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String MESSAGE_ID = (String) headers.get(AmqpHeaders.MESSAGE_ID);
        boolean multiple = false;
        channel.basicAck(deliveryTag, multiple);
    }

    /**
     * 执行定时任务的触发器
     * @param job
     * @throws TaskException
     * @throws SchedulerException
     */
    public void executeTrigger(SJobEntity job) throws TaskException, SchedulerException {
        job.setJob_name(job.getJob_name()+"["+job.getJob_serial_id()+"]");
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
        ScheduleUtils.createScheduleJobSimpleTrigger(scheduler, job, TentantEnableJob.class);
    }

    /**
     * 执行一个用于触发的时间
     * @param job
     * @throws TaskException
     * @throws SchedulerException
     */
    public void executeCronTrigger (SJobEntity job) throws TaskException, SchedulerException {
        ScheduleUtils.createScheduleJobCron(scheduler, job, TentantEnableJob.class);
    }
}
