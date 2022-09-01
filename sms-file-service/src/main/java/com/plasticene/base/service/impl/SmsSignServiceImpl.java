package com.plasticene.base.service.impl;

import com.plasticene.base.client.SmsClient;
import com.plasticene.base.constant.SmsSignConstant;
import com.plasticene.base.dao.SmsSignDAO;
import com.plasticene.base.dto.SmsSignReq;
import com.plasticene.base.entity.SmsSign;
import com.plasticene.base.factory.SmsClientFactory;
import com.plasticene.base.param.SmsSignParam;
import com.plasticene.base.service.SmsSignService;
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
 * @date 2022/8/31 18:10
 */
@Service
public class SmsSignServiceImpl implements SmsSignService {
    @Resource
    private SmsSignDAO smsSignDAO;
    @Resource
    private SmsClientFactory smsClientFactory;
    @Resource
    private IdGenerator idGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsSign(SmsSignParam param) {
        SmsSign smsSign = PtcBeanUtils.copy(param, SmsSign.class);
        long id = idGenerator.nextId();
        smsSign.setId(id);
        smsSignDAO.insert(smsSign);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditSmsSign(Long signId) {
        SmsSign smsSign = get(signId);
        SmsClient smsClient = smsClientFactory.getSmsClient(smsSign.getChannelType());
        SmsSignReq signReq = new SmsSignReq();
        signReq.setSignName(smsSign.getName());
        signReq.setRemark(smsSign.getRemark());
        SmsResult smsResult = smsClient.addSign(signReq);
        setSignStatus(signId, smsResult);
    }

    public SmsSign get(Long signId) {
        return smsSignDAO.selectById(signId);
    }

    void setSignStatus(Long signId, SmsResult smsResult) {
        String code = smsResult.getCode();
        SmsSign smsSign = new SmsSign();
        smsSign.setId(signId);
        if (Objects.equals(code, "OK")) {
            smsSign.setStatus(SmsSignConstant.SIGN_STATUS_AUDITING);
        } else {
            smsSign.setStatus(SmsSignConstant.SIGN_STATUS_FAIL);
            smsSign.setFailReason(smsResult.getMessage());
        }
        smsSignDAO.updateById(smsSign);
    }
}
