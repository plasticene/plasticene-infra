package com.plasticene.base.config;

import com.plasticene.base.listener.RedisKeyExpiredListener;
import com.plasticene.boot.redis.core.listener.AbstractChannelMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/5 17:53
 */
@Configuration
@Slf4j
public class RedisConfig {

//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(@Autowired RedisConnectionFactory redisConnectionFactory) {
//        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
//        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
//        return redisMessageListenerContainer;
//    }

//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
//                                                                       List<AbstractChannelMessageListener<?>> listeners) {
//        // 创建 RedisMessageListenerContainer 对象
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        // 设置 RedisConnection 工厂。
//        container.setConnectionFactory(redisConnectionFactory);
//        // 添加监听器
//        listeners.forEach(listener -> {
//            container.addMessageListener(listener, new ChannelTopic(listener.getChannel()));
//            log.info("[redisMessageListenerContainer][注册 Channel({}) 对应的监听器({})]",
//                    listener.getChannel(), listener.getClass().getName());
//        });
//        return container;
//    }

    @Bean
    public RedisKeyExpiredListener keyExpiredListener(@Autowired RedisConnectionFactory redisConnectionFactory,
                                                      RedisMessageListenerContainer redisMessageListenerContainer) {
        return new RedisKeyExpiredListener(redisMessageListenerContainer);
    }

}
