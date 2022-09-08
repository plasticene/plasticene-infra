package com.plasticene.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.plasticene.base.dao")
@EnableRabbit    //要想监听队列接收消息必须开启此注解
public class SmsFileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsFileServiceApplication.class, args);
    }

}
