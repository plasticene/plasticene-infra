package com.plasticene.base.controller;

import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.service.SmsPlanService;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
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
 * @date 2022/8/30 16:38
 */
@Api(tags = "发送短信管理")
@RestController
@ResponseResultBody
@RequestMapping("/sms/send")
public class SmsSendController {

    @Resource
    private SmsSendService smsSendService;
    @Resource
    private SmsPlanService smsPlanService;

    @ApiOperation("发送单条短信")
    @PostMapping
    public void sendSms(@RequestBody SendSmsParam param) {
        smsSendService.sendSms(param);
    }

    @ApiOperation("创建短信计划")
    @PostMapping("/plan")
    public void addSmsPlan(@RequestBody SmsPlanParam param) {
        smsPlanService.addSmsPlan(param);
    }



}
