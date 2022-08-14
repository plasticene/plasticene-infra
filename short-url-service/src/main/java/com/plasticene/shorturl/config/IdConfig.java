package com.plasticene.shorturl.config;

import cn.hutool.core.util.IdUtil;
import com.plasticene.boot.common.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/12 15:36
 */
@Configuration
public class IdConfig {

    @Value("${ptc.datacenter-id : 0}")
    private Long datacenterId;
    @Value("${ptc.worker-id : 0}")
    private Long workerId;

    @Bean
    public IdGenerator idGenerator() {
        IdGenerator idGenerator = new IdGenerator(datacenterId, workerId);
        return idGenerator;
    }

}
