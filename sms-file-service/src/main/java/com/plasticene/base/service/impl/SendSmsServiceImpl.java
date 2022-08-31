package com.plasticene.base.service.impl;

import com.plasticene.base.client.SmsClient;
import com.plasticene.base.factory.SmsClientFactory;
import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.service.SendSmsService;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:46
 */
@Service
public class SendSmsServiceImpl implements SendSmsService {

    @Resource
    private SmsClientFactory smsClientFactory;
    @Override
    public void sendSms(SendSmsParam param) {
        SmsClient smsClient = smsClientFactory.getSmsClient(param.getType());
        SmsResult smsResult = smsClient.sendSms(param.getMobile(), param.getSignName(), param.getTemplateCode(), JsonUtils.toJsonString(param.getParams()));
        System.out.println(smsResult);
    }
}
