package com.plasticene.base.factory;

import com.plasticene.base.client.SmsClient;
import com.plasticene.base.message.SmsChannelMessage;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 11:09
 */
public interface SmsClientFactory {

    SmsClient getSmsClient(Long channelId);

    SmsClient getSmsClient(Integer channelType);

    void saveSmsClient(SmsChannelMessage message);

    void delSmsClient(List<Long> channelIds);

}
