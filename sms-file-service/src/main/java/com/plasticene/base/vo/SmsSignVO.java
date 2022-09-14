package com.plasticene.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/1 15:35
 */
@Data
public class SmsSignVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("签名渠道类型：0：阿里云   1：云片  2：腾讯云")
    private Integer channelType;

    @ApiModelProperty("业务类型")
    private Integer bizType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("失败原因")
    private String failReason;

    @ApiModelProperty("渠道id")
    private Long channelId;
}
