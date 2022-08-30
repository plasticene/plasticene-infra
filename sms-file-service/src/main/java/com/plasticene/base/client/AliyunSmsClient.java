package com.plasticene.base.client;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 13:40
 */
@Slf4j
@Component
public class AliyunSmsClient implements SmsClient{

    @Value("${aliyun-sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun-sms.accessSecret}")
    private String accessSecret;

    @Value("${aliyun-sms.regionId}")
    private String regionId;




    @Override
    public SmsResult sendSms(String mobile, String signName, String templateCode, String params) {
        SmsResult smsResult = null;
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(mobile);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam(params);
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            smsResult = PtcBeanUtils.copy(response, SmsResult.class);
            return smsResult;
        } catch (ClientException e) {

        }
        return null;
    }
}
