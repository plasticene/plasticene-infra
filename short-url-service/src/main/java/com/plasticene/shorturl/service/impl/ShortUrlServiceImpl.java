package com.plasticene.shorturl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.shorturl.UrlLinkVO;
import com.plasticene.shorturl.dao.UrlLinkDAO;
import com.plasticene.shorturl.dto.UrlLinkDTO;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.param.ShortUrlParam;
import com.plasticene.shorturl.query.UrlLinkQuery;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.UniqueCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/10 23:45
 */

@Service
public class ShortUrlServiceImpl extends ServiceImpl<UrlLinkDAO, UrlLink> implements ShortUrlService {


    @Value("${ptc.domain}")
    private String domain;

    @Resource
    private UrlLinkDAO urlLinkDAO;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private UniqueCodeService uniqueCodeService;
    @Resource
    private RedisTemplate redisTemplate;

    private static final String SHORT_LONG_MAP = "short_long_url";
    private static final String LONG_MD5_CODE_MAP = "long_md5_short";

    @Override
    public String generateShortUrl(String longUrl) {
        // 1.检验url和合法性
        if (!isValidUrl(longUrl)) {
            throw new BizException("无效的url");
        }
        // 2.判断url是否已经生成过短链接，有直接返回
        Object value = redisTemplate.opsForHash().get(LONG_MD5_CODE_MAP, longUrl);
        if (Objects.nonNull(value)) {
            return domain + value.toString();
        }
        // 3.生成短链接
        long id = idGenerator.nextId();
        String uniqueCode = uniqueCodeService.getUniqueCode();
        String longUrlMd5 = DigestUtils.md5DigestAsHex(longUrl.getBytes());
        String shortUrl = domain + uniqueCode;
        UrlLink urlLink = new UrlLink();
        urlLink.setId(id);
        urlLink.setUniqueCode(uniqueCode);
        urlLink.setShortUrl(shortUrl);
        urlLink.setLongUrl(longUrl);
        urlLink.setLongUrlMd5(longUrlMd5);
        urlLinkDAO.insert(urlLink);
        // 短链接→长链接
        redisTemplate.opsForHash().put(SHORT_LONG_MAP, uniqueCode, longUrl);
        // 长链接md5→短链接压缩码
        redisTemplate.opsForHash().put(LONG_MD5_CODE_MAP, longUrlMd5, uniqueCode);
        return shortUrl;
    }

    @Override
    public String getOriginUrl(String uniqueCode) {
        Object value = redisTemplate.opsForHash().get(SHORT_LONG_MAP, uniqueCode);
        if (Objects.nonNull(value)) {
            return value.toString();
        }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UrlLinkVO> batchGenerateUrl(List<ShortUrlParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return null;
        }
        params.forEach(param -> {
            if (!isValidUrl(param.getLongUrl().trim())) {
                throw new BizException("无效的url");
            }
        });

        // todo 缓存长链接和短链接之间的数据
        List<String> longUrls = params.parallelStream().map(param -> param.getLongUrl().trim()).distinct().collect(Collectors.toList());
        List<String> uniqueCodes = uniqueCodeService.getUniqueCode(longUrls.size());
        Map<String, String> codeToLongUrlMap = new HashMap<>();
        Map<String, String> longUrlToCodeMap = new HashMap<>();
        List<UrlLink> urlLinks = new ArrayList<>();
        for(int i = 0 ; i < uniqueCodes.size(); i++) {
            String uniqueCode = uniqueCodes.get(i);
            String longUrl = longUrls.get(i);
            codeToLongUrlMap.put(uniqueCode, longUrl);
            longUrlToCodeMap.put(longUrl, uniqueCode);
            long id = idGenerator.nextId();
            String longUrlMd5 = DigestUtils.md5DigestAsHex(longUrl.getBytes());
            String shortUrl = domain + uniqueCode;
            UrlLink urlLink = new UrlLink();
            urlLink.setId(id);
            urlLink.setUniqueCode(uniqueCode);
            urlLink.setShortUrl(shortUrl);
            urlLink.setLongUrl(longUrl);
            urlLink.setLongUrlMd5(longUrlMd5);
            urlLinks.add(urlLink);
        }
        saveBatch(urlLinks);
        redisTemplate.opsForHash().putAll(SHORT_LONG_MAP, codeToLongUrlMap);
        List<UrlLinkVO> urlLinkVOList = new ArrayList<>();
        params.forEach(param -> {
            UrlLinkVO urlLinkVO = PtcBeanUtils.copy(param, UrlLinkVO.class);
            String uniqueCode = longUrlToCodeMap.get(urlLinkVO.getLongUrl().trim());
            urlLinkVO.setUniqueCode(uniqueCode);
            urlLinkVO.setShortUrl(domain + uniqueCode);
            urlLinkVOList.add(urlLinkVO);
        });
        return urlLinkVOList;
    }

    @Override
    public void batchDelUrlLink(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // todo 同步删除链接相关缓存，回收压缩码
        urlLinkDAO.deleteBatchIds(ids);
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
