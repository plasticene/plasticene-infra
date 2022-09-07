package com.plasticene.base.strategy;

import com.plasticene.base.entity.SmsTemplate;

import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 15:46
 * 一般短信服务平台发送短信时，如果模板有参数变量都要求必传的。
 * 所以这里的无论那种拒绝策略，都要保证最终通过的要把所有参数传进去。
 *
 */
public interface SmsSendRejectStrategy {

    void reject(SmsTemplate smsTemplate, Map<String, Object>params);
}
