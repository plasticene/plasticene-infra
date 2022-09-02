package com.plasticene.base.enums;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 16:19
 */
public enum SmsSendRejectStrategyEnum {
    /**
     * 忽略,即全部发送
     */
    IGNORE,
    /**
     * 至少有一个匹配
     */
    ANY_MATCH,
    /**
     * 完全匹配
     */
    FULL_MATCH;
}
