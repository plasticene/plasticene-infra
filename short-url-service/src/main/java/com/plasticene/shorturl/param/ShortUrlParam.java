package com.plasticene.shorturl.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/8 23:49
 */
@Data
public class ShortUrlParam {
    @ApiModelProperty("长连接地址")
    private String longUrl;
}
