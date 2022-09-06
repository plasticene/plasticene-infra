package com.plasticene.base.constant;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 16:29
 */
public interface SmsConstant {

    String SMS_PLAN_KEY = "sms_plan_key:";
    String SMS_PLAN_VALUE = "sms_plan_value:";

    String DEFAULT_PLACE_HOLDER_REGEX = "\\{[a-z].*?\\}";

    String DEFAULT_PLACE_HOLDER_KEY_REGEX = "[^(\\{)|(\\})]+";

    Integer EXECUTE_TYPE_NOW = 0;
    Integer EXECUTE_TYPE_DELAY = 1;

    Integer SMS_SEND_STATUS_INIT = 0;
    Integer SMS_SEND_STATUS_SUCCESS = 1;
    Integer SMS_SEND_STATUS_FAIL = -1;
}
