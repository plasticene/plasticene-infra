package com.plasticene.shorturl.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/15 14:48
 */
@Configuration
public class RedissonConfig {
    @Resource
    private RedissonClient redissonClient;

    private static final String UNIQUE_CODE_BLOOM_FILTER_KEY = "bf:unique-code";


    @Bean
    public RBloomFilter<Object> rBloomFilter() {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(UNIQUE_CODE_BLOOM_FILTER_KEY);
        bloomFilter.tryInit(10000, 0.03);
        return bloomFilter;
    }
}
