package com.perfect.task.job;

import com.perfect.mq.rabbitmq.mqenum.MQEnum;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 获取mq的定时任务信息，启动相应的定时任务
 */
@Component
@Slf4j
public class TaskJobReceiver {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = MQEnum.MqInfo.task.queueCode, durable = "true"),
            exchange = @Exchange(name=MQEnum.MqInfo.task.exchange, durable = "true", type = "topic"),
            key = MQEnum.MqInfo.task.routing_key
        )
    )
    @RabbitHandler
    public void onTaskScheduled(@Payload Object ob, @Headers Map<String, Object> headers, Channel channel)
        throws IOException {

        /**
         * Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
         * 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
         * RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
         */
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        /**
         *  multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认
         *  如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认
         */
        boolean multiple = false;
        //ACK,确认一条消息已经被消费
        channel.basicAck(deliveryTag,multiple);
    }
}
