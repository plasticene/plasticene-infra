package com.plasticene.base.factory;

import com.plasticene.base.client.SmsClient;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 11:09
 */
public interface SmsClientFactory {

    SmsClient getSmsClient(Long signId);

    SmsClient getSmsClient(String signCode);

    SmsClient getSmsClient(Integer channelType);

}
