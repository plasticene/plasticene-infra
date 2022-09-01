package com.plasticene.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.plasticene.base.dao")
public class SmsFileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsFileServiceApplication.class, args);
    }

}
