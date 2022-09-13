package com.plasticene.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.plasticene.boot.mybatis.core.handlers.JsonStringSetTypeHandler;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/5 10:22
 */
@Data
@TableName(autoResultMap = true)
public class SmsPlan extends BaseDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 签名id
     */
    private Long signId;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 手机号
     */
    @TableField(typeHandler = JsonStringSetTypeHandler.class)
    private Set<String> mobiles;

    /**
     * 执行类型：0：立即执行   1：定时执行
     */
    private Integer executeType;

    /**
     * 定时发送指定的时间
     */
    private Date sendTime;

    /**
     * 变量参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> params;

    /**
     * 总数
     */
    private Integer totalCount;

    /**
     * 成功发送数
     */
    private Integer successCount;



}
