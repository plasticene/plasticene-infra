package com.plasticene.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 16:10
 */
@Data
public class SmsRecord extends BaseDO {

    private Long id;

    private Long signId;

    private String signCode;

    private Long templateId;

    private String templateCode;

    private Integer templateType;

    private String content;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> params;

    private String apiTemplateCode;

    private String mobile;

    private Integer sendStatus;

    private Date sendTime;

    private String sendMsg;

    private String apiSendCode;

    private String apiSendMsg;

    private String apiRequestId;

    private String apiSerialNo;

    private Integer receiveStatus;

    private Date receiveTime;

    private String apiReceiveCode;

    private String apiReceiveMsg;

    private Long planId;


}
