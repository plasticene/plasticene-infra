package com.plasticene.base.client;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.plasticene.base.config.AliyunClientProperties;
import com.plasticene.base.constant.SmsSignConstant;
import com.plasticene.base.dto.SmsCallbackDTO;
import com.plasticene.base.dto.SmsSignReq;
import com.plasticene.base.dto.SmsTemplateReq;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.utils.JsonUtils;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import lombok.extern.slf4j.Slf4j;


import java.util.*;

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

    /**
     * ?????????????????????????????? ???????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????????????????????????????
     * @param mobiles
     * @param signName
     * @param templateCode
     * @param params
     * @return
     */
    @Override
    public SmsResult batchSendSms(List<String> mobiles, String signName, String templateCode, String params) {
        SmsResult smsResult = new SmsResult();
        Map map = JsonUtils.parseObject(params, Map.class);
        List<String> signNames = new ArrayList<>();
        List<Map> paramList = new ArrayList<>();
        for (int i = 0; i < mobiles.size(); i++) {
            signNames.add(signName);
            paramList.add(map);
        }
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        request.setPhoneNumberJson(JsonUtils.toJsonString(mobiles));
        request.setSignNameJson(JsonUtils.toJsonString(signNames));
        request.setTemplateCode(templateCode);
        request.setTemplateParamJson(JsonUtils.toJsonString(paramList));
        try {
            SendBatchSmsResponse response = client.getAcsResponse(request);
            smsResult = PtcBeanUtils.copy(response, SmsResult.class);
        } catch (ClientException e) {
            smsResult.setCode(e.getErrCode());
            smsResult.setMessage(e.getErrMsg());
            smsResult.setRequestId(e.getRequestId());
        }
        return smsResult;
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
            // ?????????????????????????????????????????????????????????????????????code==OK???????????????????????????????????????????????????????????????????????????message
            AddSmsSignResponse response = client.getAcsResponse(request);
            SmsResult smsResult = PtcBeanUtils.copy(response, SmsResult.class);
            return smsResult;
        } catch (ClientException e) {
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            log.error("?????????????????????????????????:", e);
            throw new BizException("?????????????????????????????????");
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
            log.error("?????????????????????????????????:", e);
            throw new BizException("?????????????????????????????????");
        }
    }

    @Override
    public void checkSignStatus() {

    }

    @Override
    public void checkTemplateStatus() {

    }

    @Override
    public List<SmsCallbackDTO> handleSmsSendCallback(JSONArray jsonArray) {
        List<SmsCallbackDTO> smsCallbackDTOS = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject rst = jsonArray.getJSONObject(i);
            SmsCallbackDTO smsCallback = new SmsCallbackDTO();
            smsCallback.setSuccess(Boolean.valueOf(rst.getString("success")));
            smsCallback.setMobile(rst.getString("phone_number"));
            smsCallback.setErrorCode(rst.getString("err_code"));
            smsCallback.setErrorMsg(rst.getString("err_msg"));
            smsCallback.setReceiveTime(DateUtil.parse(rst.getString("report_time")));
            smsCallback.setPlatformId(rst.getString("biz_id"));
            smsCallbackDTOS.add(smsCallback);
        }
        return smsCallbackDTOS;
    }

    public static void main(String[] args) {
        Map<String,Object> params = new HashMap<>();
        params.put("name", "shepherd");
        List<Map<String, Object>> paramList = new ArrayList<>();
        paramList.add(params);
        paramList.add(params);
        System.out.println(JsonUtils.toJsonString(paramList));
        List<String> list = new ArrayList<>();
        list.add(JsonUtils.toJsonString(params));
        list.add(JsonUtils.toJsonString(params));
        System.out.println(JsonUtils.toJsonString(list));
    }


}
