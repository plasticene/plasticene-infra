package com.plasticene.base.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.plasticene.base.entity.SmsRecord;
import com.plasticene.boot.web.core.anno.FieldMask;
import com.plasticene.boot.web.core.enums.MaskEnum;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/7 16:13
 */
@Data
public class SmsRecordDTO {

    private Long id;

    private Long signId;

    private String signCode;

    private Long templateId;

    private String templateCode;

    private Integer templateType;

    private String content;

    private Map<String, Object> params;

    private Integer channelType;

    @FieldMask(MaskEnum.MOBILE_PHONE)
    private String mobile;

    private Integer sendStatus;

    private Date sendTime;

    private String sendMsg;

    private String apiSendCode;

    private String apiSendMsg;

    private String apiRequestId;

    private String platformId;

    private Integer receiveStatus;

    private Date receiveTime;

    private String apiReceiveCode;

    private String apiReceiveMsg;

    private Long planId;

}
