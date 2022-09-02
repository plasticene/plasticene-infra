package com.plasticene.base.param;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/1 16:06
 */
@Data
public class SmsTemplateParam {

    /**
     * 模版自定义code，供业务团队使用模板
     */
    private String code;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 模版内容
     */
    private String content;

    /**
     * 模板变量参数
     */
    private Set<String> params;

    /**
     * 备注
     */
    private String remark;


    /**
     * 短信类型 0：验证码  1：通知  2：营销推广
     */
    private Integer type;

    /**
     * 签名id
     */
    private Long signId;


}
