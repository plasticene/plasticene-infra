package com.plasticene.base.service.impl;

import com.plasticene.base.dao.SmsChannelDAO;
import com.plasticene.base.entity.SmsChannel;
import com.plasticene.base.param.SmsChannelParam;
import com.plasticene.base.service.SmsChannelService;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 10:06
 */
@Service
public class SmsChannelServiceImpl implements SmsChannelService {
    @Resource
    private SmsChannelDAO smsChannelDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsChannel(SmsChannelParam param) {
        SmsChannel smsChannel = PtcBeanUtils.copy(param, SmsChannel.class);
        smsChannelDAO.insert(smsChannel);
    }
}
