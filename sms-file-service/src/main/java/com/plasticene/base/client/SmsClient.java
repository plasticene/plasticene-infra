package com.plasticene.base.client;

import com.plasticene.base.vo.SmsResult;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 11:10
 */
public interface SmsClient {

    SmsResult sendSms(String mobile, String signName, String templateCode, String params);



}
