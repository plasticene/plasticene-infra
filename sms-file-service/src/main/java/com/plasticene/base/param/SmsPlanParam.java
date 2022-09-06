package com.plasticene.base.param;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/5 16:44
 */
@Data
public class SmsPlanParam {
    private Set<String> mobiles;
    private Long templateId;
    private Long signId;
    private Integer executeType;
    private Map<String, Object> params;
    private Date sendTime;
}
