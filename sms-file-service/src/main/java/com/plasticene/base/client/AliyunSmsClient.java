package com.plasticene.base.client;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.plasticene.base.config.AliyunClientProperties;
import com.plasticene.base.dto.SmsSignReq;
import com.plasticene.base.dto.SmsTemplateReq;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 13:40
 */
@Slf4j
public class AliyunSmsClient implements SmsClient{

    private AliyunClientProperties properties;

    private IAcsClient client;

    public AliyunSmsClient(AliyunClientProperties properties) {
        this.properties = properties;
        DefaultProfile profile = DefaultProfile.getProfile(properties.getRegionId(), properties.getAccessKeyId(),
                properties.getAccessSecret());
        this.client =  new DefaultAcsClient(profile);

    }





    @Override
    public SmsResult sendSms(String mobile, String signName, String templateCode, String params) {
        SmsResult smsResult = new SmsResult();

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(mobile);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam(params);
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            smsResult = PtcBeanUtils.copy(response, SmsResult.class);
        } catch (ClientException e) {
            smsResult.setCode(e.getErrCode());
            smsResult.setMessage(e.getErrMsg());
            smsResult.setRequestId(e.getRequestId());
        }
        return smsResult;
    }

    @Override
    public SmsResult batchSendSms(List<SmsResult> mobiles, String signName, String templateCode, String params) {
        return null;
    }

    @Override
    public void addSign(SmsSignReq signReq) {
        AddSmsSignRequest request = new AddSmsSignRequest();
        request.setSignName(signReq.getSignName());
        request.setSignSource(signReq.getSignSource());
        request.setRemark(signReq.getRemark());
        request.setSignFileLists(signReq.getFileList());
        try {
            AddSmsSignResponse response = client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("调用阿里云添加签名失败:", e);
            throw new BizException("调用阿里云添加签名失败");
        }
    }

    @Override
    public void addTemplate(SmsTemplateReq templateReq) {
        AddSmsTemplateRequest request = new AddSmsTemplateRequest();
        request.setTemplateType(templateReq.getType());
        request.setTemplateName(templateReq.getName());
        request.setTemplateContent(templateReq.getContent());
        request.setRemark(templateReq.getRemark());
        try {
            AddSmsTemplateResponse response = client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("调用阿里云添加模板失败:", e);
            throw new BizException("调用阿里云添加模板失败");
        }

    }

    @Override
    public void checkSignStatus() {

    }

    @Override
    public void checkTemplateStatus() {

    }


}
