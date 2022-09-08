package com.plasticene.base.listener;

import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.service.SmsSendService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/8 14:21
 */
@Component
@Slf4j
@RabbitListener(queues = {"sms.send.now.queue"})
public class SmsSendListener {

    @Resource
    private SmsSendService smsSendService;

    @RabbitHandler
    public void listener(SmsPlanDTO smsPlanDTO, Message message, Channel channel) throws IOException {
        log.info("=====准备执行短信计划planId：{}, 发送短信", smsPlanDTO.getPlanId());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            smsSendService.addSmsRecordAndSendSms(smsPlanDTO);
            channel.basicAck(deliveryTag,false);
        } catch (Exception e){
            //basicNack与basicReject一样，只是多了一个参数控制是否可以批量拒绝
            //channel.basicNack(deliveryTag, true, true);
            channel.basicReject(deliveryTag,true);
        }

    }
}
