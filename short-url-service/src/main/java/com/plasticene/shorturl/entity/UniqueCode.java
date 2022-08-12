package com.plasticene.shorturl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/12 13:55
 */
@Data
public class UniqueCode extends BaseDO {

    private Long id;
    /**
     * 唯一压缩码
     */
    private String code;

    /**
     * 压缩码状态：0：未使用  1：已使用   -1：已失效
     */
    private Integer status;

    /**
     * 生成方式：0：随机数   1：分布式id   2：hash
     */
    private Integer type;
}
