package com.perfect.mq.consumer.sys.config.tentant;

import com.perfect.bean.pojo.mqsender.MqSenderPojo;
import com.perfect.framework.utils.mq.MessageUtil;
import com.perfect.mq.rabbitmq.mqenum.MQEnum;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
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
public class TentantConsumer {

    /**
     * 配置监听的哪一个队列，同时在没有queue和exchange的情况下会去创建并建立绑定关系
     * @param messageDataObject
     * @param headers
     * @param channel
     * @throws IOException
     */
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = MQEnum.MqInfo.TentantTask.queueCode, durable = "true"),
            exchange = @Exchange(name=MQEnum.MqInfo.TentantTask.exchange, durable = "true", type = "topic"),
            key = MQEnum.MqInfo.TentantTask.routing_key
        )
    )
    @RabbitHandler
    public void onMessage(@Payload Message messageDataObject, @Headers Map<String, Object> headers, Channel channel, CorrelationData correlationData)
        throws IOException {
        MqSenderPojo mqSenderPojo = MessageUtil.getMessageBodyBean(messageDataObject);

        Object messageContext = MessageUtil.getMessageContextBean(messageDataObject);

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String MESSAGE_ID = (String) headers.get(AmqpHeaders.MESSAGE_ID);
        boolean multiple = false;
        channel.basicAck(deliveryTag, multiple);
    }

    /**
     * 开始启用定时任务quartz
     * @param messageContext
     */
    private void process(Object messageContext){

    }
}
