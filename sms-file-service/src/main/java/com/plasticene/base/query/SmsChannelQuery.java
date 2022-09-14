package com.plasticene.base.query;

import com.plasticene.boot.common.pojo.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 10:24
 */
@Data
public class SmsChannelQuery extends PageParam {
    @ApiModelProperty("渠道名称")
    private String name;

    @ApiModelProperty("渠道类型 0：阿里云  1：云片  2：腾讯云")
    private Integer type;
}
