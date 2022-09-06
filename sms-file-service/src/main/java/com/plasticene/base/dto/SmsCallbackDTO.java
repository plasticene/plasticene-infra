package com.plasticene.base.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/6 16:23
 */
@Data
public class SmsCallbackDTO {
    /**
     * 是否接收成功
     */
    private Boolean success;
    /**
     * API 接收结果的编码
     */
    private String errorCode;
    /**
     * API 接收结果的说明
     */
    private String errorMsg;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户接收时间
     */
    private Date receiveTime;

    /**
     * 短信 API 发送返回的序号
     */
    private String platformId;
}
