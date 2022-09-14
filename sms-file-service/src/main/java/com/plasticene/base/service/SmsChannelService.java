package com.plasticene.base.service;

import com.plasticene.base.param.SmsChannelParam;
import com.plasticene.base.query.SmsChannelQuery;
import com.plasticene.base.vo.SmsChannelVO;
import com.plasticene.boot.common.pojo.PageResult;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 10:05
 */
public interface SmsChannelService {

    void addSmsChannel(SmsChannelParam param);

    void updateSmsChannel(Long channelId, SmsChannelParam param);

    void deleteSmsChannel(List<Long> channelIds);

    PageResult<SmsChannelVO> getList(SmsChannelQuery query);
}
