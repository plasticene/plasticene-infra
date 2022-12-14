package com.plasticene.base.vo;

import com.plasticene.boot.web.core.anno.FieldMask;
import com.plasticene.boot.web.core.enums.MaskEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 10:21
 */
@Data
public class SmsChannelVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("渠道名称")
    private String name;

    @ApiModelProperty("渠道类型 0：阿里云  1：云片  2：腾讯云")
    private Integer type;

    @ApiModelProperty("api key")
    private String apiKey;

    @ApiModelProperty("api秘钥")
    @FieldMask(MaskEnum.API_SECRET)
    private String apiSecret;
}
