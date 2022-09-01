package com.plasticene.base.entity;

import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 10:13
 */
@Data
public class SmsSign extends BaseDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 签名自定义唯一码，方便业务团队使用签名
     */
    private String code;

    /**
     * 签名名称
     */
    private String name;

    /**
     * 是否审核通过 0：否  1：是
     */
    private Integer status;

    /**
     * 签名渠道类型：0：阿里云   1：云片  2：腾讯云
     */
    private Integer channelType;

    /**
     * 业务类型
     */
    private Integer bizType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 失败原因
     */
    private String failReason;
}
