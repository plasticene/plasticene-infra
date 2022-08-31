package com.plasticene.base.service.impl;

import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.service.SendSmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SendSmsServiceImplTest {
    @Resource
    private SendSmsService sendSmsService;

    @Test
    public void sendSms() {
        SendSmsParam param = new SendSmsParam();
        param.setType(0);
        param.setMobile("17816875939");
        param.setSignName("shepherd");
        param.setTemplateCode("SMS_193518103");
        Map<String, Object> map = new HashMap<>();
        map.put("code", "083150");
        param.setParams(map);
        sendSmsService.sendSms(param);

    }
}