package com.plasticene.base.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 14:59
 */
@Api(tags = "短信模板管理")
@RestController
@ResponseResultBody
@RequestMapping("/sms/template")
public class smsTemplateController {

    @PostMapping
    @ApiOperation("添加模板")
    public void addSmsTemplate() {

    }
}
