package com.plasticene.base.controller;

import com.plasticene.base.dto.SmsTemplateDTO;
import com.plasticene.base.param.SmsTemplateParam;
import com.plasticene.base.service.SmsTemplateService;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 14:59
 */
@Api(tags = "短信模板管理")
@RestController
@ResponseResultBody
@RequestMapping("/sms/template")
public class SmsTemplateController {

    @Resource
    private SmsTemplateService smsTemplateService;

    @PostMapping
    @ApiOperation("添加模板")
    public void addSmsTemplate(@RequestBody SmsTemplateParam param) {
        smsTemplateService.addSmsTemplate(param);
    }

    @PutMapping("/audit/{templateId}")
    @ApiOperation("审核短信模板")
    public void auditSmsTemplate(@PathVariable("templateId") Long templateId) {
        smsTemplateService.auditSmsTemplate(templateId);
    }

    @GetMapping
    @ApiOperation("模板列表")
    public List<SmsTemplateDTO> getList() {
        // todo
        return null;
    }

    @DeleteMapping
    @ApiOperation("删除模板(批量)")
    public void delTemplate(@RequestBody List<Long> templateIds) {
        // todo
    }
}
