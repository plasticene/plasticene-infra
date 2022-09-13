package com.plasticene.base.listener;

import com.plasticene.base.factory.SmsClientFactory;
import com.plasticene.base.message.SmsChannelMessage;
import com.plasticene.boot.redis.core.listener.AbstractChannelMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 15:49
 */
@Component
@Slf4j
public class SmsChannelListener extends AbstractChannelMessageListener<SmsChannelMessage> {
    @Resource
    private SmsClientFactory smsClientFactory;
    @Override
    public void onMessage(SmsChannelMessage message) {
        log.info("[收到 SmsChannel 刷新消息], message:{}", message);
        if (CollectionUtils.isEmpty(message.getDelChannelIds())) {
            smsClientFactory.saveSmsClient(message);
        } else {
            smsClientFactory.delSmsClient(message.getDelChannelIds());
        }
    }
}
