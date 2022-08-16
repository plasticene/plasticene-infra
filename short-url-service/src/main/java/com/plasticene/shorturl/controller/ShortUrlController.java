package com.plasticene.shorturl.controller;

import com.plasticene.boot.common.exception.BizException;
import com.plasticene.shorturl.param.ShortUrlParam;
import com.plasticene.shorturl.service.ShortUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/8 23:45
 */
@RestController
@ResponseBody
@Api(tags = "短链接管理")
@RequestMapping("/api")
public class ShortUrlController {

    @Resource
    private ShortUrlService shortUrlService;

    @PostMapping("/short/url")
    @ApiOperation("生成短链接地址")
    public String generateShortUrl(@RequestBody @Validated ShortUrlParam param){
        String url = param.getLongUrl().trim();
        String shortUrl = shortUrlService.generateShortUrl(url);
        return shortUrl;
    }
}
