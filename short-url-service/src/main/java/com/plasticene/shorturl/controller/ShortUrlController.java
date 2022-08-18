package com.plasticene.shorturl.controller;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.UrlLinkVO;
import com.plasticene.shorturl.dto.UrlLinkDTO;
import com.plasticene.shorturl.param.BatchUrlParam;
import com.plasticene.shorturl.param.ShortUrlParam;
import com.plasticene.shorturl.query.UrlLinkQuery;
import com.plasticene.shorturl.service.ShortUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/8 23:45
 */
@RestController
@ResponseBody
@Api(tags = "短链接管理")
@RequestMapping("/api/short")
public class ShortUrlController {

    @Resource
    private ShortUrlService shortUrlService;

    @PostMapping("/url")
    @ApiOperation("生成短链接地址")
    public String generateShortUrl(@RequestBody @Validated ShortUrlParam param){
        String url = param.getLongUrl().trim();
        String shortUrl = shortUrlService.generateShortUrl(url);
        return shortUrl;
    }

    @PostMapping("/url/batch")
    @ApiOperation("批量生成短链接")
    public List<UrlLinkVO> batchGenerateShortUrl(@RequestBody @Validated BatchUrlParam param) {
        List<UrlLinkVO> urlLinkVOS = shortUrlService.batchGenerateUrl(param.getUrlList());
        return urlLinkVOS;
    }

    @DeleteMapping("/url")
    @ApiOperation("删除短链接映射(批量)")
    public void delUrlLink(@RequestBody List<Long> ids) {
        shortUrlService.batchDelUrlLink(ids);
    }

    @GetMapping("/url")
    @ApiOperation("链接映射列表")
    public PageResult<UrlLinkDTO> getList(UrlLinkQuery query) {
        PageResult<UrlLinkDTO> pageResult = shortUrlService.getList(query);
        return pageResult;
    }
}
