package com.plasticene.base.service;

import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.base.param.SmsTemplateParam;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/1 16:21
 */
public interface SmsTemplateService {

    void addSmsTemplate(SmsTemplateParam param);

    void auditSmsTemplate(Long templateId);

    SmsTemplate get(Long templateId);
}
