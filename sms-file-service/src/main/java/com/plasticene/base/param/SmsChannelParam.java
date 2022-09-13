package com.plasticene.base.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/9 18:08
 */
@Data
public class SmsChannelParam {

    @ApiModelProperty("渠道名称")
    private String name;

    @ApiModelProperty("渠道类型 0：阿里云  1：云片  2：腾讯云")
    private Integer type;

    @ApiModelProperty("api key")
    private String apiKey;

    @ApiModelProperty("api秘钥")
    private String apiSecret;
}
