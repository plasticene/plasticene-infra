package com.plasticene.base.service.impl;

import com.plasticene.base.client.SmsClient;
import com.plasticene.base.dao.SmsRecordDAO;
import com.plasticene.base.entity.SmsRecord;
import com.plasticene.base.entity.SmsSign;
import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.base.factory.SmsClientFactory;
import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.base.service.SmsSignService;
import com.plasticene.base.service.SmsTemplateService;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:46
 */
@Service
public class SendSmsServiceImpl implements SmsSendService {

    @Resource
    private SmsClientFactory smsClientFactory;
    @Resource
    private SmsTemplateService smsTemplateService;
    @Resource
    private SmsSignService smsSignService;
    @Resource
    private SmsRecordDAO smsRecordDAO;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(SendSmsParam param) {
        SmsTemplate smsTemplate = smsTemplateService.get(param.getTemplateId());
        SmsSign smsSign = smsSignService.get(smsTemplate.getSignId());
        addSmsRecord(param, smsTemplate, smsSign);
//        SmsClient smsClient = smsClientFactory.getSmsClient(smsSign.getChannelType());
//        SmsResult smsResult = smsClient.sendSms(param.getMobile(), smsSign.getName(), smsTemplate.getApiTemplateCode(), JsonUtils.toJsonString(param.getParams()));
    }

    void addSmsRecord(SendSmsParam param, SmsTemplate smsTemplate, SmsSign smsSign) {
        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setMobile(param.getMobile());
        smsRecord.setTemplateId(smsTemplate.getId());
        smsRecord.setSignId(smsSign.getId());
        smsRecord.setTemplateType(smsTemplate.getType());
        smsRecord.setParams(param.getParams());
        smsRecord.setContent(smsTemplate.getContent());
        smsRecordDAO.insert(smsRecord);

    }

}
