package com.plasticene.shorturl.controller;

import com.plasticene.shorturl.param.ShortUrlParam;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/8 23:45
 */
@RestController
@ResponseBody
@Api(tags = "短链接管理")
@RequestMapping
public class ShortUrlController {

    @PostMapping("/short/url")
    public void generateShortUrl(@RequestBody @Validated ShortUrlParam param){


    }
}
