package com.plasticene.base.controller;

import com.plasticene.base.param.SendSmsParam;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:38
 */
@Api(tags = "发送短信管理")
@RestController
@ResponseResultBody
@RequestMapping("/sms/send")
public class SmsSendController {

    @ApiOperation("发送单条短信")
    public void sendSms(@RequestBody SendSmsParam param) {

    }
}
