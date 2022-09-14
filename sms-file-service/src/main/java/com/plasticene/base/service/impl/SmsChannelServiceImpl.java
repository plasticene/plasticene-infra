package com.plasticene.base.service.impl;

import com.plasticene.base.dao.SmsChannelDAO;
import com.plasticene.base.entity.SmsChannel;
import com.plasticene.base.message.SmsChannelMessage;
import com.plasticene.base.param.SmsChannelParam;
import com.plasticene.base.query.SmsChannelQuery;
import com.plasticene.base.service.SmsChannelService;
import com.plasticene.base.vo.SmsChannelVO;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.boot.redis.core.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 10:06
 */
@Service
public class SmsChannelServiceImpl implements SmsChannelService {
    @Resource
    private SmsChannelDAO smsChannelDAO;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsChannel(SmsChannelParam param) {
        SmsChannel smsChannel = PtcBeanUtils.copy(param, SmsChannel.class);
        smsChannel.setId(idGenerator.nextId());
        smsChannelDAO.insert(smsChannel);
        sendSmsChannelRefreshMessage(smsChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSmsChannel(Long channelId, SmsChannelParam param) {
        SmsChannel smsChannel = PtcBeanUtils.copy(param, SmsChannel.class);
        smsChannel.setId(channelId);
        smsChannelDAO.updateById(smsChannel);
        sendSmsChannelRefreshMessage(smsChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSmsChannel(List<Long> channelIds) {
        if (CollectionUtils.isEmpty(channelIds)) {
            return;
        }
        smsChannelDAO.deleteBatchIds(channelIds);
        sendSmsChannelRefreshMessage(channelIds);
    }

    @Override
    public PageResult<SmsChannelVO> getList(SmsChannelQuery query) {
        LambdaQueryWrapperX<SmsChannel> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(SmsChannel::getName, query.getName());
        PageParam pageParam = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<SmsChannel> pageResult = smsChannelDAO.selectPage(pageParam, queryWrapper);
        List<SmsChannelVO> smsChannelVOS = toSmsChannelVOList(pageResult.getList());
        PageResult<SmsChannelVO> result = new PageResult<>();
        result.setList(smsChannelVOS);
        result.setTotal(pageResult.getTotal());
        result.setPages(pageResult.getPages());
        return result;
    }

    List<SmsChannelVO> toSmsChannelVOList(List<SmsChannel> smsChannels) {
        List<SmsChannelVO> smsChannelVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(smsChannels)) {
            return smsChannelVOS;
        }
        smsChannels.forEach(smsChannel -> {
            SmsChannelVO smsChannelVO = PtcBeanUtils.copy(smsChannel, SmsChannelVO.class);
            smsChannelVOS.add(smsChannelVO);
        });
        return smsChannelVOS;
    }

    public void sendSmsChannelRefreshMessage(SmsChannel smsChannel) {
        SmsChannelMessage message = PtcBeanUtils.copy(smsChannel, SmsChannelMessage.class);
        redisUtils.convertAndSend(message);
    }

    public void sendSmsChannelRefreshMessage(List<Long> channelIds) {
        SmsChannelMessage message = new SmsChannelMessage();
        message.setDelChannelIds(channelIds);
        redisUtils.convertAndSend(message);
    }
}
