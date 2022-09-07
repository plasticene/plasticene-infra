package com.plasticene.base.service.impl;

import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.base.service.SmsTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/7 11:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTemplateServiceImplTest {
    @Resource
    private SmsTemplateService smsTemplateService;


    @Test
    public void get() {
        SmsTemplate smsTemplate = smsTemplateService.get(8445337328943104l);
        System.out.println(smsTemplate);
    }

}