package com.plasticene.base.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.plasticene.boot.mybatis.core.handlers.JsonStringSetTypeHandler;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/5 17:57
 */
@Data
public class SmsPlanDTO {
    private Long planId;
    private Long signId;
    private Long templateId;
    private Integer status;
    private List<String> mobiles;
    private Integer executeType;
    private Map<String, Object> params;
}
