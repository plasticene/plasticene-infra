package com.plasticene.base.service.impl;

import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.service.SmsSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SendSmsServiceImplTest {
    @Resource
    private SmsSendService smsSendService;
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void sendSms() {
//        SendSmsParam param = new SendSmsParam();
//        param.setType(0);
//        param.setMobile("17816875939");
//        param.setSignName("shepherd");
//        param.setTemplateCode("SMS_193518103");
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", "083150");
//        param.setParams(map);
//        smsSendService.sendSms(param);

    }

    @Test
    public void test() {
        redisTemplate.opsForValue().set("hello", "123456", 30, TimeUnit.SECONDS);
    }
}