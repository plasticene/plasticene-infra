package com.plasticene.base.client;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.plasticene.base.config.AliyunClientProperties;
import com.plasticene.base.constant.SmsSignConstant;
import com.plasticene.base.dto.SmsSignReq;
import com.plasticene.base.dto.SmsTemplateReq;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import lombok.extern.slf4j.Slf4j;


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
    public SmsResult addSign(SmsSignReq signReq) {
        AddSmsSignRequest request = new AddSmsSignRequest();
        request.setSignName(signReq.getSignName());
        request.setSignSource(SmsSignConstant.ALIYUN_SOURCE_WEBSITE);
        request.setRemark(signReq.getRemark());

        List<AddSmsSignRequest.SignFileList> signFileListList = new ArrayList<>();

        AddSmsSignRequest.SignFileList signFileList = new AddSmsSignRequest.SignFileList();
        signFileList.setFileContents("R0lGODlhHAAmAKIHAKqqqsvLy0hISObm5vf394uL");
        signFileList.setFileSuffix("jpg");
        signFileListList.add(signFileList);
        request.setSignFileLists(signFileListList);
        try {
            // 正常返回响应结果，但并不代表就调用成功了，只有code==OK是成功的，其他事阿里云的检测到的不合法情况，具体看message
            AddSmsSignResponse response = client.getAcsResponse(request);
            SmsResult smsResult = PtcBeanUtils.copy(response, SmsResult.class);
            return smsResult;
        } catch (ClientException e) {
            // 正常情况只有秘钥不对，或者参数传的不对，如少传必填参数，阿里云会直接报错，其他情况都会正常返回
            log.error("调用阿里云添加签名失败:", e);
            throw new BizException("调用阿里云添加签名失败");
        }
    }

    @Override
    public SmsResult addTemplate(SmsTemplateReq templateReq) {
        AddSmsTemplateRequest request = new AddSmsTemplateRequest();
        request.setTemplateType(templateReq.getType());
        request.setTemplateName(templateReq.getName());
        request.setTemplateContent(templateReq.getContent());
        request.setRemark(templateReq.getRemark());
        try {
            AddSmsTemplateResponse response = client.getAcsResponse(request);
            SmsResult smsResult = PtcBeanUtils.copy(response, SmsResult.class);
            return smsResult;
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
