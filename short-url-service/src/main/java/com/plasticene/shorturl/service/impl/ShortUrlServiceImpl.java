package com.plasticene.shorturl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.shorturl.dao.ShortUrlDAO;
import com.plasticene.shorturl.entity.ShortUrl;
import com.plasticene.shorturl.service.ShortUrlService;
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
    private static final String DOMAIN = "http://127.0.0.1:18800/x/";

    @Resource
    private ShortUrlDAO shortUrlDAO;
    @Override
    public String generateShortUrl(String longUrl) {
        if (!isValidUrl(longUrl)) {
            throw new BizException("无效的url");
        }
        IdGenerator idGenerator = new IdGenerator(0, 0);
        long id = idGenerator.nextId();
        String uniqueCode = RandomUtils.to62RadixString(id);
        String longUrlMd5 = DigestUtils.md5DigestAsHex(longUrl.getBytes());
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(id);
        shortUrl.setUniqueCode(uniqueCode);
        shortUrl.setShortUrl(DOMAIN + uniqueCode);
        shortUrl.setLongUrl(longUrl);
        shortUrl.setLongUrlMd5(longUrlMd5);
        shortUrlDAO.insert(shortUrl);
        return DOMAIN + uniqueCode;


    }

    @Override
    public String getOriginUrl(String uniqueCode) {
        LambdaQueryWrapper<ShortUrl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShortUrl::getUniqueCode, uniqueCode);
        ShortUrl shortUrl = shortUrlDAO.selectOne(queryWrapper);
        return shortUrl == null ? null : shortUrl.getLongUrl();
    }

    public boolean isValidUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
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
