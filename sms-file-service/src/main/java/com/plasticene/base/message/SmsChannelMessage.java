package com.plasticene.base.message;

import com.plasticene.boot.redis.core.message.AbstractChannelMessage;
import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/13 14:43
 * 该消息体为空，不需要
 */
@Data
public class SmsChannelMessage extends AbstractChannelMessage {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 渠道类型
     */
    private Integer type;

    /**
     * 渠道api key
     */
    private String apiKey;

    /**
     * 渠道api秘钥
     */
    private String apiSecret;

    /**
     * 删除的渠道id集合
     */
    private List<Long> delChannelIds;


    @Override
    public String getChannel() {
        return "sms-channel-topic";
    }
}
