package com.plasticene.base.strategy;

import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.boot.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 15:56
 * 只要占位符参数匹配了短信模板中的任意一个占位符key,就允许发送
 */
@Slf4j
public class SmsSendAnyMatchStrategy implements SmsSendRejectStrategy{

    @Override
    public void reject(SmsTemplate smsTemplate, Map<String, Object> params) {
        Set<String> keySet = params.entrySet().stream().filter(entry -> Objects.nonNull(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet());
        if (CollectionUtils.intersection(smsTemplate.getParams(), keySet).size() <= 0) {
            log.error("短信占位符替换参数与短信模板完全不匹配,templateContent = {},params = {}", smsTemplate.getContent(), params);
            throw new BizException("短信占位符替换参数与短信模板完全不匹配");
        }
    }
}
