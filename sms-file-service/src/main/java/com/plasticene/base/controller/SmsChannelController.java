package com.plasticene.base.controller;

import com.plasticene.base.param.SmsChannelParam;
import com.plasticene.base.query.SmsChannelQuery;
import com.plasticene.base.vo.SmsChannelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/9 17:18
 *
 * 短信渠道 → 维护各大第三方短信平台的秘钥key信息   这些key信息相当重要不能泄露
 *
 * 通常这些信息都是维护在短信服务项目配置里面，因为一般短信服务就用同一个key去调用第三方短信平台，就比如公司内部的短信服务供其他团队调用，肯定就
 * 一个key秘钥，没必要搞多个，能调通第三方平台即可，所以根本不需要维护短信渠道。
 *
 * 那如果短信管理服务是一个saas服务，即该服务面向多家公司使用，同时公司又自己买的短信平台，但是没有管理系统，这时候各家公司接入的短信平台不一样，
 * 如我们这里支持的阿里云，云片，腾讯云等等，此时就需要各自维护短信渠道秘钥key了，这种情况少见，少见，少见，因为我都用你的短信管理系统发了，
 * 就用你们平台发短信，对接第三方短信平台是你们的事，客户压根不想关心。不过对于开发者来说，我有了key秘钥，想去试试发短信，这里就有用武之地了
 */
@Api(tags = "短信渠道管理")
@RestController
@RequestMapping("/sms/channel")
public class SmsChannelController {

    @PostMapping
    @ApiOperation("添加渠道")
    public void addSmsChannel(@RequestBody SmsChannelParam param) {

    }
    @PutMapping("/{channelId}")
    @ApiOperation("更新渠道")
    public void updateSmsChannel(@PathVariable("channelId")Long channelId, @RequestBody SmsChannelParam param) {

    }

    @DeleteMapping
    @ApiOperation("删除渠道(批量)")
    public void delSmsChannel(@RequestBody List<Long> channelIds) {

    }


    @GetMapping
    @ApiOperation("获取渠道列表")
    public List<SmsChannelVO> getList(SmsChannelQuery query) {
        return null;
    }
}
