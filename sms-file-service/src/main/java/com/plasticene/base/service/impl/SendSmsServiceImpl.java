package com.plasticene.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plasticene.base.client.SmsClient;
import com.plasticene.base.constant.SmsConstant;
import com.plasticene.base.dao.SmsPlanDAO;
import com.plasticene.base.dao.SmsRecordDAO;
import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.entity.SmsPlan;
import com.plasticene.base.entity.SmsRecord;
import com.plasticene.base.entity.SmsSign;
import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.base.factory.SmsClientFactory;
import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.base.service.SmsSignService;
import com.plasticene.base.service.SmsTemplateService;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.JsonUtils;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:46
 */
@Service
public class SendSmsServiceImpl extends ServiceImpl<SmsRecordDAO, SmsRecord> implements SmsSendService {

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
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsRecordAndSendSms(SmsPlanDTO smsPlanDTO) {
        List<SmsRecord> smsRecordList = new ArrayList<>();
        SmsTemplate smsTemplate = smsTemplateService.get(smsPlanDTO.getTemplateId());
        SmsSign smsSign = smsSignService.get(smsPlanDTO.getSignId());
        List<String> mobiles = smsPlanDTO.getMobiles();
        for(String mobile : mobiles) {
            SmsRecord smsRecord = new SmsRecord();
            smsRecord.setPlanId(smsPlanDTO.getPlanId());
            smsRecord.setMobile(mobile);
            smsRecord.setTemplateId(smsPlanDTO.getTemplateId());
            smsRecord.setSignId(smsPlanDTO.getSignId());
            smsRecord.setTemplateType(smsTemplate.getType());
            smsRecord.setParams(smsPlanDTO.getParams());
            smsRecord.setContent(smsTemplate.getContent());
            smsRecordList.add(smsRecord);
        }
        saveBatch(smsRecordList);
        SmsClient smsClient = smsClientFactory.getSmsClient(smsSign.getChannelType());
        SmsResult smsResult;
        if (smsPlanDTO.getMobiles().size() == 1) {
            smsResult = smsClient.sendSms(smsPlanDTO.getMobiles().get(0), smsSign.getName(),
                    smsTemplate.getApiTemplateCode(), JsonUtils.toJsonString(smsPlanDTO.getParams()));
        } else {
            smsResult = smsClient.batchSendSms(smsPlanDTO.getMobiles(), smsSign.getName(),
                    smsTemplate.getApiTemplateCode(), JsonUtils.toJsonString(smsPlanDTO.getParams()));
        }
        List<SmsRecord> updateRecords = new ArrayList<>();
        Integer sendStatus;
        String sendMsg;
        if (Objects.equals(smsResult.getCode(), "OK")) {
            sendStatus = SmsConstant.SMS_SEND_STATUS_SUCCESS;
            sendMsg = "成功";
        } else {
            sendStatus = SmsConstant.SMS_SEND_STATUS_FAIL;
            sendMsg = "调用异常";
        }
        smsRecordList.forEach(smsRecord -> {
            SmsRecord update = new SmsRecord();
            update.setId(smsRecord.getId());
            update.setSendStatus(sendStatus);
            update.setSendMsg(sendMsg);
            update.setSendTime(new Date());
            update.setApiSendCode(smsResult.getCode());
            update.setApiSendMsg(smsResult.getMessage());
            update.setApiRequestId(smsResult.getRequestId());
            update.setApiSerialNo(smsResult.getBizId());
            updateRecords.add(update);
        });
        updateBatchById(updateRecords);
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
