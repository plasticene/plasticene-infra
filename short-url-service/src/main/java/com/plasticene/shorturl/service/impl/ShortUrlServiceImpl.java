package com.plasticene.shorturl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.shorturl.dao.UrlLinkDAO;
import com.plasticene.shorturl.dto.UrlLinkDTO;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.query.UrlLinkQuery;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.UniqueCodeService;
import com.plasticene.shorturl.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public PageResult<UrlLinkDTO> getList(UrlLinkQuery query) {
        LambdaQueryWrapperX<UrlLink> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(UrlLink::getUniqueCode, query.getUniqueCode());
        PageParam pageParam = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<UrlLink> pageResult = urlLinkDAO.selectPage(pageParam, queryWrapper);
        List<UrlLinkDTO> urlLinkDTOList = toUrlLinkDTOList(pageResult.getList());
        PageResult<UrlLinkDTO> result = new PageResult<>();
        result.setList(urlLinkDTOList);
        result.setTotal(pageResult.getTotal());
        result.setPages(pageResult.getPages());
        return result;
    }

    List<UrlLinkDTO> toUrlLinkDTOList(List<UrlLink> urlLinks) {
        List<UrlLinkDTO> urlLinkDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(urlLinks)) {
            return urlLinkDTOList;
        }
        urlLinks.forEach(urlLink -> {
            UrlLinkDTO urlLinkDTO = PtcBeanUtils.copy(urlLink, UrlLinkDTO.class);
            urlLinkDTOList.add(urlLinkDTO);
        });
        return urlLinkDTOList;
    }


    public boolean isValidUrl(String urls) {
        boolean isUrl;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)"
                + "?([.][a-z0-9]{0,}+|/?)";
        //对比
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        //判断是否匹配
        isUrl = mat.matches();
        if (isUrl) {
            isUrl = true;
        }
        return isUrl;
    }
}
