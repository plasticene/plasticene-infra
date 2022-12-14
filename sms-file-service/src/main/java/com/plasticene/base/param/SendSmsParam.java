package com.plasticene.base.param;

import lombok.Data;

import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 16:41
 */
@Data
public class SendSmsParam {
    private String mobile;
    private Long templateId;
    private Map<String, Object> params;
}
