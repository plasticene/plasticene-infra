package com.plasticene.base.config;

import com.plasticene.base.listener.RedisKeyExpiredListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/5 17:53
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }

    @Bean
    public RedisKeyExpiredListener keyExpiredListener(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisKeyExpiredListener(this.redisMessageListenerContainer(redisConnectionFactory));
    }

}
