package com.plasticene.base.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plasticene.base.dto.SmsPlanDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/8 13:58
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitmqConfigTest {

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Test
    public void produceMessage() {
        SmsPlanDTO smsPlanDTO = new SmsPlanDTO();
        smsPlanDTO.setPlanId(1l);
        smsPlanDTO.setMobiles(Lists.newArrayList("1234","5678"));
        Map<String, Object> map = new HashMap<>();
        map.put("name", "shepherd");
        map.put("age", 26);
        smsPlanDTO.setParams(map);
        smsPlanDTO.setTemplateId(2l);
        rabbitTemplate.convertAndSend("sms-event-exchange1","sms.send.now", smsPlanDTO);
        int i = 0;
    }


}