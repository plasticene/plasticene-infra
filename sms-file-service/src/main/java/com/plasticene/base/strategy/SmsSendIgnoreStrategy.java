package com.plasticene.base.strategy;

import com.plasticene.base.entity.SmsTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 15:54
 *
 * 短信发送拒绝策略-忽略策略,无论短信发送入参与模板是否匹配,都允许发送 意味着这里不需要做任何事情
 */
public class SmsSendIgnoreStrategy implements SmsSendRejectStrategy{
    @Override
    public void reject(SmsTemplate smsTemplate, Map<String, Object> params) {

    }
}
