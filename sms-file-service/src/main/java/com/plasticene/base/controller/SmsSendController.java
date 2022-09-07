package com.plasticene.base.controller;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONArray;
import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.dto.SmsRecordDTO;
import com.plasticene.base.param.SendSmsParam;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.query.SmsPlanQuery;
import com.plasticene.base.query.SmsRecordQuery;
import com.plasticene.base.service.SmsPlanService;
import com.plasticene.base.service.SmsSendService;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @ApiOperation("短信计划列表")
    @GetMapping("/plan")
    public PageResult<SmsPlanDTO> getSmsPlanList(SmsPlanQuery query) {
        return null;
    }


    @ApiOperation("短信记录列表")
    @GetMapping("/record")
    public PageResult<SmsRecordDTO> getSmsRecordList(SmsRecordQuery query) {
        PageResult<SmsRecordDTO> pageResult = smsSendService.getList(query);
        return pageResult;
    }






}
