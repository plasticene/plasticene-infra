package com.plasticene.base.service;

import com.alibaba.fastjson.JSONArray;
import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.dto.SmsRecordDTO;
import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.query.SmsRecordQuery;
import com.plasticene.boot.common.pojo.PageResult;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:45
 */
public interface SmsSendService {

    void sendSms(SendSmsParam param);

    void addSmsRecordAndSendSms(SmsPlanDTO smsPlanDTO);

    void updateSmsReceiveStatus(Integer channelType, JSONArray jsonArray);

    PageResult<SmsRecordDTO> getList(SmsRecordQuery query);
}
