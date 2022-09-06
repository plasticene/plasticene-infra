package com.plasticene.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plasticene.base.client.SmsClient;
import com.plasticene.base.constant.SmsConstant;
import com.plasticene.base.dao.SmsPlanDAO;
import com.plasticene.base.dao.SmsRecordDAO;
import com.plasticene.base.dto.SmsCallbackDTO;
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
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
            smsRecord.setChannelType(smsSign.getChannelType());
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
            update.setPlatformId(smsResult.getBizId());
            updateRecords.add(update);
        });
        updateBatchById(updateRecords);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSmsReceiveStatus(Integer channelType, JSONArray jsonArray) {
        if (jsonArray.size() == 0) {
            return;
        }
        SmsClient smsClient = smsClientFactory.getSmsClient(channelType);
        List<SmsCallbackDTO> smsCallbackDTOList = smsClient.handleSmsSendCallback(jsonArray);
        List<String> platformIds = smsCallbackDTOList.parallelStream().map(SmsCallbackDTO::getPlatformId).distinct()
                .collect(Collectors.toList());
        List<SmsRecord> smsRecordList = getSmsRecordList(channelType, platformIds);
        if (CollectionUtils.isEmpty(smsRecordList)) {
            return;
        }
        List<SmsRecord> updateSmsRecordList = new ArrayList<>();
        smsRecordList.forEach(smsRecord -> {
            SmsCallbackDTO callback = smsCallbackDTOList.parallelStream().filter(smsCallbackDTO -> Objects.equals(smsCallbackDTO.getMobile(),
                    smsRecord.getMobile()) && Objects.equals(smsCallbackDTO.getPlatformId(), smsRecord.getPlatformId())).findFirst().orElse(null);
            if (Objects.isNull(callback)) {
                return;
            }
            SmsRecord update = new SmsRecord();
            update.setId(smsRecord.getId());
            update.setReceiveStatus(callback.getSuccess() ? SmsConstant.SMS_RECEIVE_STATUS_SUCCESS : SmsConstant.SMS_RECEIVE_STATUS_FAIL);
            update.setApiReceiveCode(callback.getErrorCode());
            update.setApiReceiveMsg(callback.getErrorMsg());
            update.setReceiveTime(callback.getReceiveTime());
            updateSmsRecordList.add(update);
        });

        if (!CollectionUtils.isEmpty(updateSmsRecordList)) {
            updateBatchById(updateSmsRecordList);
        }
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

    List<SmsRecord> getSmsRecordList(Integer channelType, List<String> platformIds) {
        LambdaQueryWrapperX<SmsRecord> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.select(SmsRecord::getId, SmsRecord::getMobile, SmsRecord::getPlatformId);
        queryWrapper.eq(SmsRecord::getChannelType, channelType);
        queryWrapper.in(SmsRecord::getPlatformId, platformIds);
        List<SmsRecord> smsRecords = smsRecordDAO.selectList(queryWrapper);
        return smsRecords;
    }



}
