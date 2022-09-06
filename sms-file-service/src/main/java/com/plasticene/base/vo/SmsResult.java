package com.plasticene.base.vo;

import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 11:18
 */
@Data
public class SmsResult {

    /**
     * API 返回错误码
     *
     * 由于第三方的错误码可能是字符串，所以使用 String 类型
     */
    private String code;
    /**
     * API 返回提示
     */
    private String message;

    /**
     * API 请求编号
     */
    private String requestId;

    private String bizId;
}
