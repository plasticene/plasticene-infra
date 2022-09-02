package com.plasticene.base.service;

import com.plasticene.base.entity.SmsSign;
import com.plasticene.base.param.SmsSignParam;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/31 18:10
 */
public interface SmsSignService {

    void addSmsSign(SmsSignParam param);

    void auditSmsSign(Long signId);

    SmsSign get(Long signId);
}
