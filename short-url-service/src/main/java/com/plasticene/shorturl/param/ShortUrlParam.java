package com.plasticene.shorturl.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/8 23:49
 */
@Data
public class ShortUrlParam {
    @ApiModelProperty("长连接地址")
    @NotBlank(message = "长地址不能为空")
    private String longUrl;
}
