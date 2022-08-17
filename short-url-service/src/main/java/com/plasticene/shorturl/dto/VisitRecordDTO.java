package com.plasticene.shorturl.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/17 13:51
 */
@Data
public class VisitRecordDTO {
    private Long id;
    private Long urlLinkId;
    private String uniqueCode;
    private String clientId;
    private String clientIp;
    private Date visitTime;
    private String userAgent;
    private String phoneType;
    private String browserType;
    private String browserVersion;
    private String osType;
    private String osVersion;
    private String deviceType;
    private String country;
    private String province;
    private String city;
    private String isp;
    private Date createTime;
    private Date updateTime;
}
