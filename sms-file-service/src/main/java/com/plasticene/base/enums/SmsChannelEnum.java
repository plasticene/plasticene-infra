package com.plasticene.base.enums;

import lombok.Getter;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 11:38
 */
@Getter
public enum SmsChannelEnum {

    ALIYUN(0, "阿里云"),
    YUNPIAN(1, "云片"),
    TENCENT(2, "腾讯");

    private Integer type;
    private String name;

    SmsChannelEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

}
