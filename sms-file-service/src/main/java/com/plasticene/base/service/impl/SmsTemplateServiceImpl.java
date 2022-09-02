package com.plasticene.base.service.impl;

import com.plasticene.base.client.SmsClient;
import com.plasticene.base.constant.SmsTemplateConstant;
import com.plasticene.base.dao.SmsTemplateDAO;
import com.plasticene.base.dto.SmsTemplateReq;
import com.plasticene.base.entity.SmsSign;
import com.plasticene.base.entity.SmsTemplate;
import com.plasticene.base.factory.SmsClientFactory;
import com.plasticene.base.param.SmsTemplateParam;
import com.plasticene.base.service.SmsSignService;
import com.plasticene.base.service.SmsTemplateService;
import com.plasticene.base.vo.SmsResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/1 16:29
 */
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {
    @Resource
    private SmsTemplateDAO smsTemplateDAO;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private SmsClientFactory smsClientFactory;
    @Resource
    private SmsSignService smsSignService;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsTemplate(SmsTemplateParam param) {
        SmsTemplate smsTemplate = PtcBeanUtils.copy(param, SmsTemplate.class);
        long id = idGenerator.nextId();
        smsTemplate.setId(id);
        smsTemplateDAO.insert(smsTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditSmsTemplate(Long templateId) {
        SmsTemplate smsTemplate = get(templateId);
        SmsSign smsSign = smsSignService.get(smsTemplate.getSignId());
        SmsTemplateReq templateReq = PtcBeanUtils.copy(smsTemplate, SmsTemplateReq.class);
        SmsClient smsClient = smsClientFactory.getSmsClient(smsSign.getChannelType());
        SmsResult smsResult = smsClient.addTemplate(templateReq);
        setTemplateStatus(templateId, smsResult);
    }


    @Override
    public SmsTemplate get(Long templateId) {
        return smsTemplateDAO.selectById(templateId);
    }

    void setTemplateStatus(Long templateId, SmsResult smsResult) {
        String code = smsResult.getCode();
        SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setId(templateId);
        if (Objects.equals(code, "OK")) {
            smsTemplate.setStatus(SmsTemplateConstant.TEMPLATE_STATUS_AUDITING);
        } else {
            smsTemplate.setStatus(SmsTemplateConstant.TEMPLATE_STATUS_FAIL);
            smsTemplate.setFailReason(smsResult.getMessage());
        }
        smsTemplateDAO.updateById(smsTemplate);
    }
}
