package com.plasticene.base.handler;

import com.plasticene.base.entity.SmsTemplate;

import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 15:30
 */
public interface SmsTemplatePlaceHolderHandler {

    String handle(SmsTemplate smsTemplate, Map<String, Object> params);
}
