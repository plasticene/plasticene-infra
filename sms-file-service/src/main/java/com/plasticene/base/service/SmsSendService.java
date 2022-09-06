package com.plasticene.base.service;

import com.alibaba.fastjson.JSONArray;
import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.param.SmsPlanParam;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:45
 */
public interface SmsSendService {

    void sendSms(SendSmsParam param);

    void addSmsRecordAndSendSms(SmsPlanDTO smsPlanDTO);

    void updateSmsReceiveStatus(Integer channelType, JSONArray jsonArray);
}
