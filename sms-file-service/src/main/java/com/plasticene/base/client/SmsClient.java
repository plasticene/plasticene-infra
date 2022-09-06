package com.plasticene.base.client;

import com.plasticene.base.dto.SmsSignReq;
import com.plasticene.base.dto.SmsTemplateReq;
import com.plasticene.base.vo.SmsResult;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 11:10
 */
public interface SmsClient {

    SmsResult sendSms(String mobile, String signName, String templateCode, String params);

    SmsResult batchSendSms(List<String> mobiles, String signName, String templateCode, String params);

    SmsResult addSign(SmsSignReq signReq);

    SmsResult addTemplate(SmsTemplateReq templateReq);

    void checkSignStatus();

    void checkTemplateStatus();





}
