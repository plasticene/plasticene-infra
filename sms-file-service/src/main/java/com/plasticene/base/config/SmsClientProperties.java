package com.plasticene.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 14:17
 */
@Component
@Data
public class SmsClientProperties {
    @Resource
    private AliyunClientProperties aliyunClientProperties;
}
