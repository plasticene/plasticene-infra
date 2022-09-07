package com.plasticene.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.plasticene.boot.mybatis.core.handlers.JsonStringSetTypeHandler;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/30 18:51
 */

@Data
@TableName(autoResultMap = true)
public class SmsTemplate extends BaseDO {

    /**
     *主键
     */
    private  Long id;

    /**
     * 模版自定义code，供业务团队使用模板
     */
    private String code;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 是否审批通过   0：否   1：是
     */
    private Integer status;

    /**
     * 模版内容
     */
    private String content;

    /**
     * 模板变量参数
     */

    @TableField(typeHandler = JsonStringSetTypeHandler.class)
    private Set<String> params;

    /**
     * 备注
     */
    private String remark;

    /**
     *模版审核失败原因
     */
    private String failReason;

    /**
     * 签名id
     */
    private Long signId;

    /**
     * 签名code
     */
    private String signCode;

    /**
     * 服务方的短信模版code
     */
    private String apiTemplateCode;

    /**
     * 短信类型 0：验证码  1：通知  2：营销推广
     */
    private Integer type;

    /**
     * 渠道类型 0：阿里云  1：云片  2：腾讯
     */
//    private Integer channelType;







}
