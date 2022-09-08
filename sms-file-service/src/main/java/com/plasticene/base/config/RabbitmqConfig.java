package com.plasticene.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/8 10:58
 */
@Configuration
@Slf4j
public class RabbitmqConfig {

    private static final String SMS_EXCHANGE = "sms-event-change";
    private static final String SMS_SEND_NOW_QUEUE = "sms.send.now.queue";
    private static final String SMS_SEND_DELAY_QUEUE = "sms.send.delay.queue";
    private static final String SMS_SEND_NOW_ROUTE_KEY = "sms.send.now";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct // 在RabbitmqConfig对象创建完之后执行
    public void initRabbitMq() {
        /**
         * 设置发送消息到mq服务器即broker，回调判断发送成功与否，这个时候需要开启配置：
         * 老版本的配置是：spring.rabbitmq.publisher-confirms=true   新版本中已经过时
         * 新版本的配置是：spring.rabbitmq.publisher-confirm-type=correlated
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData:当前消息数据的唯一id，需要我们在发送消息时候指定
             * @param b 是否发送成功
             * @param s 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                log.info("correlationData={}, b={}, s={}", correlationData, b, s);
            }
        });

        /**
         * 设置MQ服务器把消息从交换机路由到队列是否成功回调，这个时候需要开启配置：
         * spring.rabbitmq.publisher-returns=true
         * spring.rabbitmq.template.mandatory=true
         * 该方法只有在消息没有正常抵达队列才会触发回调该方法
         */
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             *
             * @param message 失败的消息的内容
             * @param replyCode 回复的状态码
             * @param replyText 回复的文本内容
             * @param exchange 消息发送的交换机
             * @param routeKey 消息发送的路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routeKey) {
                log.info("message={}, repayCode={}, replyText={}, exchange={}, routeKey={}", message, replyCode, replyText, exchange, routeKey);

            }
        });

    }

    /**
     * 设置MQ发送消息的序列化规则
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange smsEventExchange() {
        /**
         *   String name,
         *   boolean durable,
         *   boolean autoDelete,
         *   Map<String, Object> arguments
         */
        return new TopicExchange("sms-event-exchange", true, false);
    }

    /**
     * 普通队列
     *
     * @return
     */
    @Bean
    public Queue smsSendNowQueue() {
        Queue queue = new Queue("sms.send.now.queue", true, false, false);
        return queue;
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue smsSendDelayQueue() {
        /**
         Queue(String name,  队列名字
         boolean durable,  是否持久化
         boolean exclusive,  是否排他
         boolean autoDelete, 是否自动删除
         Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        //死信交换机
        arguments.put("x-dead-letter-exchange", "sms-event-exchange");
        //死信路由键
        arguments.put("x-dead-letter-routing-key", "sms.send.now");
        // 这里不值得队列的过期时间，因为演示任务每个任务过期时间是自定义指定，需要在发送消息时候指定
        // arguments.put("x-message-ttl", 60000); // 消息过期时间 1分钟
        return new Queue("sms.send.delay.queue",true,false,false, arguments);
    }

    @Bean
    public Binding smsSendNowBinding() {
        return new Binding("sms.send.now.queue",
                Binding.DestinationType.QUEUE,
                "sms-event-exchange",
                "sms.send.now",
                null);
    }

    @Bean
    public Binding smsSendDelayBinding() {
        return new Binding("sms.send.delay.queue",
                Binding.DestinationType.QUEUE,
                "sms-event-exchange",
                "sms.send.delay",
                null);
    }

}
