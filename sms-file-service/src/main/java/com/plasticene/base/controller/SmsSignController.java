package com.plasticene.base.controller;

import com.plasticene.base.dto.SmsSignDTO;
import com.plasticene.base.param.SmsSignParam;
import com.plasticene.base.service.SmsSignService;
import com.plasticene.base.vo.SmsSignVO;
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
 * 短信签名和短信模板都需要第三方平台审核通过的，这里可以直接添加已经审核通过的,此时状态为审核通过可用状态，
 * 如果是添加权限的签名或者模板，就需要调用第三平台接口添加签名或者模板，等着平台审核结果，由于各个平台的审核
 * 状态查询或者回调方式都不一样，很难适配，这里统一让管理员操作，管理员通过去第三方平台查看签名或者模板的审核结果，然后在
 * 界面上更新状态和审核结果，原因啥的。。。
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
    public List<SmsSignVO> getList() {
        // todo
        return null;
    }

    @DeleteMapping
    @ApiOperation("删除签名(批量)")
    public void delSign(@RequestBody List<Long> signIds) {
        // todo
    }
}
