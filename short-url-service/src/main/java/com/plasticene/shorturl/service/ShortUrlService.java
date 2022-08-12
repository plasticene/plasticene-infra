package com.plasticene.shorturl.service;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/10 23:43
 */
public interface ShortUrlService {

    String generateShortUrl(String longUrl);

    String getOriginUrl(String uniqueCode);

}
