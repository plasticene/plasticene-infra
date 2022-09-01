package com.plasticene.base.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 17:55
 */
@Data
public class SmsSignParam {


    @ApiModelProperty("签名名称")
    private String name;

    @ApiModelProperty("签名code")
    private String code;

    @ApiModelProperty("签名渠道类型：0：阿里云   1：云片  2：腾讯云")
    private Integer channelType;

    @ApiModelProperty("业务类型")
    private Integer bizType;

    @ApiModelProperty("备注")
    private String remark;
}
