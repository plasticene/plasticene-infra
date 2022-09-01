package com.plasticene.base.controller;

import com.plasticene.base.dto.SmsSignDTO;
import com.plasticene.base.param.SmsSignParam;
import com.plasticene.base.service.SmsSignService;
import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 17:53
 */
@Api(tags = "短信签名管理")
@RestController
@ResponseResultBody
@RequestMapping("/sms/sign")
public class SmsSignController {
    @Resource
    private SmsSignService smsSignService;

    @PostMapping
    @ApiOperation("添加签名")
    public void addSmsSign(@RequestBody SmsSignParam param) {
        smsSignService.addSmsSign(param);
    }

    @PutMapping("/audit/{signId}")
    @ApiOperation("审核签名")
    public void auditSmsSign(@PathVariable("signId") Long signId) {
        smsSignService.auditSmsSign(signId);
    }

    @GetMapping
    @ApiOperation("签名列表")
    public List<SmsSignDTO> getList() {
        // todo
        return null;
    }

    @DeleteMapping
    @ApiOperation("删除签名(批量)")
    public void delSign(@RequestBody List<Long> signIds) {
        // todo
    }
}
