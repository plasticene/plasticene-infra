package com.plasticene.shorturl.service;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.UrlLinkVO;
import com.plasticene.shorturl.dto.UrlLinkDTO;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.param.ShortUrlParam;
import com.plasticene.shorturl.query.UrlLinkQuery;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/10 23:43
 */
public interface ShortUrlService {

    String generateShortUrl(String longUrl);

    String getOriginUrl(String uniqueCode);

    UrlLink getUrlLink(String uniqueCode);

    PageResult<UrlLinkDTO> getList(UrlLinkQuery query);

    List<UrlLinkVO> batchGenerateUrl(List<ShortUrlParam> params);

    void batchDelUrlLink(List<Long> ids);

}
