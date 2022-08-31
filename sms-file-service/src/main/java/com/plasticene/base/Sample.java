package com.plasticene.base;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/29 18:42
 */


public class Sample {


    public static void main(String[] args) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                "LTAI4GHpv2y35PBVWUt42fJ61",
                "VoF8212psWmcsb424bKVeg7eMZkKzA");

        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("17816875939");
        request.setSignName("shepherd");
        request.setTemplateCode("SMS_1935181031");
        request.setTemplateParam("{\"code\":\"345890\"}");

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}

