package com.plasticene.base.dto;

import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 10:53
 */
@Data
public class SmsTemplateReq {
    private Integer type;
    private String name;
    private String content;
    private String remark;
}
