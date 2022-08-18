package com.plasticene.shorturl;

import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/18 15:58
 */
@Data
public class UrlLinkVO {
    private String longUrl;
    private Long requestId;
    private String shortUrl;
    private String uniqueCode;
}
