package com.plasticene.base.factory;

import com.plasticene.base.enums.SmsSendRejectStrategyEnum;
import com.plasticene.base.strategy.SmsSendAnyMatchStrategy;
import com.plasticene.base.strategy.SmsSendFullMatchStrategy;
import com.plasticene.base.strategy.SmsSendIgnoreStrategy;
import com.plasticene.base.strategy.SmsSendRejectStrategy;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 16:18
 */
public class SmsSendRejectStrategyFactory {

    private static final SmsSendIgnoreStrategy IGNORE_STRATEGY = new SmsSendIgnoreStrategy();

    private static final SmsSendAnyMatchStrategy ANY_MATCH_STRATEGY = new SmsSendAnyMatchStrategy();

    private static final SmsSendFullMatchStrategy FULL_MATCH_STRATEGY = new SmsSendFullMatchStrategy();

    public static SmsSendRejectStrategy getStrategy(SmsSendRejectStrategyEnum strategyEnum) {
        switch (strategyEnum) {
            case IGNORE:
                return IGNORE_STRATEGY;
            case ANY_MATCH:
                return ANY_MATCH_STRATEGY;
            case FULL_MATCH:
                return FULL_MATCH_STRATEGY;
        }
        return null;
    }
}
