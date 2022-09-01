package com.plasticene.base.factory;

import com.plasticene.base.client.AliyunSmsClient;
import com.plasticene.base.client.SmsClient;
import com.plasticene.base.client.TencentSmsClient;
import com.plasticene.base.client.YunPianSmsClient;
import com.plasticene.base.config.SmsClientProperties;
import com.plasticene.base.enums.SmsChannelEnum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 11:33
 */
public class DefaultSmsClientFactory implements SmsClientFactory {

    private SmsClientProperties smsClientProperties;

    private final ConcurrentMap<Integer, SmsClient> signTypeClients = new ConcurrentHashMap<>();

    public DefaultSmsClientFactory(SmsClientProperties smsClientProperties) {
        this.smsClientProperties = smsClientProperties;
        Stream.of(SmsChannelEnum.values()).forEach(channel -> {
            Integer type = channel.getType();
            SmsClient smsClient = createSmsClient(channel);
            signTypeClients.put(type, smsClient);
        });

    }

    SmsClient createSmsClient(SmsChannelEnum smsChannelEnum) {
        switch (smsChannelEnum) {
            case ALIYUN:
                return new AliyunSmsClient(smsClientProperties.getAliyunClientProperties());
            case YUNPIAN:
                return new YunPianSmsClient();
            case TENCENT:
                return new TencentSmsClient();
        }
        return null;
    }



    @Override
    public SmsClient getSmsClient(Long signId) {
        return null;
    }

    @Override
    public SmsClient getSmsClient(String signCode) {
        return null;
    }

    @Override
    public SmsClient getSmsClient(Integer channelType) {
        return signTypeClients.get(channelType);
    }
}
