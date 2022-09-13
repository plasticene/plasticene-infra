package com.plasticene.base.factory;

import com.plasticene.base.client.AliyunSmsClient;
import com.plasticene.base.client.SmsClient;
import com.plasticene.base.client.TencentSmsClient;
import com.plasticene.base.client.YunPianSmsClient;
import com.plasticene.base.config.AliyunClientProperties;
import com.plasticene.base.config.SmsClientProperties;
import com.plasticene.base.enums.SmsChannelEnum;
import com.plasticene.base.message.SmsChannelMessage;
import org.springframework.util.CollectionUtils;

import java.util.List;
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

    private final ConcurrentMap<Long, SmsClient> channelIdClients = new ConcurrentHashMap<>();

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

    SmsClient createSmsClient(SmsChannelMessage message) {
        Integer type = message.getType();
        SmsChannelEnum smsChannelEnum = SmsChannelEnum.getType(type);
        switch (smsChannelEnum) {
            case ALIYUN:
            {
                AliyunClientProperties properties = new AliyunClientProperties();
                properties.setAccessKeyId(message.getApiKey());
                properties.setAccessSecret(message.getApiSecret());
                return new AliyunSmsClient(properties);
            }
            case YUNPIAN:
                return new YunPianSmsClient();
            case TENCENT:
                return new TencentSmsClient();
        }
        return null;
    }


    @Override
    public SmsClient getSmsClient(Long channelId) {
        return channelIdClients.get(channelId);
    }

    @Override
    public SmsClient getSmsClient(Integer channelType) {
        return signTypeClients.get(channelType);
    }

    @Override
    public void saveSmsClient(SmsChannelMessage message) {
        SmsClient smsClient = createSmsClient(message);
        channelIdClients.put(message.getId(), smsClient);
    }

    @Override
    public void delSmsClient(List<Long> channelIds) {
        if (CollectionUtils.isEmpty(channelIds)) {
            return;
        }
        channelIds.forEach(channelId -> {
            channelIdClients.remove(channelId);
        });


    }
}
