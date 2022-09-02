package com.plasticene.base.strategy;

import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.boot.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 15:57
 * 短信发送拒绝策略-完全匹配,只有当短信入参与短信模板占位符完全匹配时才允许发送
 */
@Slf4j
public class SmsSendFullMatchStrategy implements SmsSendRejectStrategy{
    @Override
    public void reject(SmsTemplate smsTemplate, Map<String, Object> params) {
        Set<String> keySet = params.entrySet().stream().filter(entry -> Objects.nonNull(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet());
        if (!keySet.containsAll(smsTemplate.getParams())) {
            log.error("短信占位符替换参数与短信模板不完全匹配,templateContent = {}, params = {}", smsTemplate.getContent(), params);
            throw new BizException("短信占位符替换参数与短信模板不完全匹配");
        }
    }
}
