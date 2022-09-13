package com.plasticene.base.entity;

import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/9 17:54
 */
@Data
public class SmsChannel extends BaseDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 渠道类型
     */
    private Integer type;

    /**
     * 渠道api key
     */
    private String apiKey;

    /**
     * 渠道api秘钥
     */
    private String apiSecret;
}
