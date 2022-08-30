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
     * 签名唯一码，用于签名使用
     */
    private String code;

    /**
     * 签名名称
     */
    private String name;

    /**
     * 启用开关 0：关闭  1：关闭
     */
    private String status;

    /**
     * 签名类型：0：阿里云   1：腾讯云  2：云片短信
     */
    private Integer type;

    /**
     * 业务类型
     */
    private Integer bizType;

    /**
     * 备注
     */
    private String remark;
}
