package com.plasticene.base.handler;

import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.base.strategy.SmsSendRejectStrategy;
import com.plasticene.base.utils.PlaceHolderUtils;

import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 15:45
 */
public class DefaultSmsTemplatePlaceHolderHandler implements SmsTemplatePlaceHolderHandler{

    private SmsSendRejectStrategy rejectStrategy;

    public DefaultSmsTemplatePlaceHolderHandler(SmsSendRejectStrategy rejectStrategy) {
        this.rejectStrategy = rejectStrategy;
    }
    @Override
    public String handle(SmsTemplate smsTemplate, Map<String, Object> params) {
        rejectStrategy.reject(smsTemplate, params);
        String content = PlaceHolderUtils.replacePlaceHolder(smsTemplate, params);
        return content;
    }
}
