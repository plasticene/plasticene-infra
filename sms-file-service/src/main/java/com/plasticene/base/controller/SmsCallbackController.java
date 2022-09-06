package com.plasticene.base.controller;

import com.alibaba.fastjson.JSONArray;
import com.plasticene.base.enums.SmsChannelEnum;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.boot.common.pojo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/6 15:51
 */
@Api(tags = "短信回调管理")
@RestController
@RequestMapping("/sms/callback")
public class SmsCallbackController {
    @Resource
    private SmsSendService smsSendService;

    @PostMapping("/aliyun")
    @ApiOperation("阿里云短信回调")
    public ResponseVO aliyunSendSmsCallback(@RequestBody JSONArray jsonArray) {
        smsSendService.updateSmsReceiveStatus(SmsChannelEnum.ALIYUN.getType(), jsonArray);
        return new ResponseVO(0,"接收成功");
    }
}
