package com.plasticene.base.constant;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/1 11:35
 * 0：企事业单位的全称或简称。
 * 1：工信部备案网站的全称或简称。
 * 2：App应用的全称或简称。
 * 3：公众号或小程序的全称或简称。
 * 4：电商平台店铺名的全称或简称。
 * 5：商标名的全称或简称。
 */
public interface SmsSignConstant {
    Integer ALIYUN_SOURCE_COMPANY = 0;
    Integer ALIYUN_SOURCE_WEBSITE = 1;
    Integer ALIYUN_SOURCE_APP = 2;
    Integer ALIYUN_SOURCE_OFFICIAL_ACCOUNT = 3;
    Integer ALIYUN_SOURCE_PLATFORM = 4;
    Integer ALIYUN_SOURCE_BRAND = 5;

    Integer SIGN_STATUS_INIT = 0;

    Integer SIGN_STATUS_AUDITING = 1;

    Integer SIGN_STATUS_PASS = 2;

    Integer SIGN_STATUS_FAIL = -1;
}
