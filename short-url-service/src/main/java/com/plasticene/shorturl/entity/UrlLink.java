package com.plasticene.shorturl.entity;

import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/11 16:55
 */
@Data
public class UrlLink extends BaseDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 唯一压缩码
     */
    private String uniqueCode;

    /**
     * 短链接地址
     */
    private String shortUrl;

    /**
     * 原始长连接地址
     */
    private String longUrl;

    /**
     * 长链接地址
     */
    private String longUrlMd5;
}
