package com.plasticene.shorturl.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/17 16:37
 */
@Data
public class UrlLinkDTO {
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

    private Date createTime;
    private Date updateTime;
}
