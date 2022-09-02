package com.plasticene.base.strategy;

import com.plasticene.base.entity.SmsTemplate;

import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 16:15
 */
public class SmsSendFillNullStrategy implements SmsSendRejectStrategy{
    @Override
    public void reject(SmsTemplate smsTemplate, Map<String, Object> params) {

    }
}
