package com.plasticene.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 14:30
 */

@Configuration
@ConfigurationProperties(prefix = "sms.aliyun")
@Data
public class AliyunClientProperties {
    private String accessKeyId;
    private String accessSecret;
    private String regionId="cn-hangzhou";
}
