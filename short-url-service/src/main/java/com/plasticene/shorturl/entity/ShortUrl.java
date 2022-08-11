package com.plasticene.shorturl.entity;

import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/11 16:55
 */
@Data
public class ShortUrl extends BaseDO {
    private Long id;
    private String uniqueCode;
    private String shortUrl;
    private String longUrl;
    private String longUrlMd5;
}
