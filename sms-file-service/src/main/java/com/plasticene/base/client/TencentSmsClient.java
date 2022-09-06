package com.plasticene.base.client;

import com.plasticene.base.dto.SmsSignReq;
import com.plasticene.base.dto.SmsTemplateReq;
import com.plasticene.base.vo.SmsResult;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 14:36
 */
public class TencentSmsClient implements SmsClient{
    @Override
    public SmsResult sendSms(String mobile, String signName, String templateCode, String params) {
        return null;
    }

    @Override
    public SmsResult batchSendSms(List<String> mobiles, String signName, String templateCode, String params) {
        return null;
    }

    @Override
    public SmsResult addSign(SmsSignReq signReq) {
        return null;
    }

    @Override
    public SmsResult addTemplate(SmsTemplateReq templateReq) {
        return null;
    }

    @Override
    public void checkSignStatus() {

    }

    @Override
    public void checkTemplateStatus() {

    }
}
