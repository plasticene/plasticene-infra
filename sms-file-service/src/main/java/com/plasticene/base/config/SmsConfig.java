package com.plasticene.base.config;

import com.plasticene.base.factory.DefaultSmsClientFactory;
import com.plasticene.base.factory.SmsClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 14:12
 */
@Configuration
public class SmsConfig {

    @Resource
    private SmsClientProperties smsClientProperties;

    @Bean
    public SmsClientFactory smsClientFactory() {
        return new DefaultSmsClientFactory(smsClientProperties);
    }
}
