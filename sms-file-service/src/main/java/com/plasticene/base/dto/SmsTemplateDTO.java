package com.plasticene.base.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.boot.mybatis.core.handlers.JsonStringSetTypeHandler;
import lombok.Data;

import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 14:04
 */
@Data
public class SmsTemplateDTO{

    private  Long id;

    private String name;

    private Integer status;

    private String content;

    private Set<String> params;

    private String remark;

    private String failReason;


    private Long signId;


    private String signCode;


    private String apiTemplateCode;


    private Integer type;


   private Integer channelType;
}
