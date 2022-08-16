package com.plasticene.shorturl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.shorturl.dao.UrlLinkDAO;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.UniqueCodeService;
import com.plasticene.shorturl.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/10 23:45
 */

@Service
public class ShortUrlServiceImpl implements ShortUrlService {


    private static final String DOMAIN = "http://127.0.0.1:18800/";

    @Resource
    private UrlLinkDAO urlLinkDAO;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private UniqueCodeService uniqueCodeService;

    @Override
    public String generateShortUrl(String longUrl) {
        if (!isValidUrl(longUrl)) {
            throw new BizException("无效的url");
        }
        long id = idGenerator.nextId();
        String uniqueCode = uniqueCodeService.getUniqueCode();
        String longUrlMd5 = DigestUtils.md5DigestAsHex(longUrl.getBytes());
        String shortUrl = DOMAIN + uniqueCode;
        UrlLink urlLink = new UrlLink();
        urlLink.setId(id);
        urlLink.setUniqueCode(uniqueCode);
        urlLink.setShortUrl(DOMAIN + uniqueCode);
        urlLink.setLongUrl(longUrl);
        urlLink.setLongUrlMd5(longUrlMd5);
        urlLinkDAO.insert(urlLink);
        return shortUrl;


    }

    @Override
    public String getOriginUrl(String uniqueCode) {
        UrlLink urlLink = getUrlLink(uniqueCode);
        return urlLink == null ? null : urlLink.getLongUrl();
    }

    @Override
    public UrlLink getUrlLink(String uniqueCode) {
        LambdaQueryWrapper<UrlLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UrlLink::getUniqueCode, uniqueCode);
        UrlLink urlLink = urlLinkDAO.selectOne(queryWrapper);
        return urlLink;
    }


    public boolean isValidUrl(String urls) {
        boolean isurl;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)"
                + "?([.][a-z0-9]{0,}+|/?)";
        //对比
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        //判断是否匹配
        isurl = mat.matches();
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }
}
