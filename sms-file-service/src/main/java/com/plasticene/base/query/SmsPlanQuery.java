package com.plasticene.base.query;

import com.plasticene.boot.common.pojo.PageParam;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/6 13:58
 */
@Data
public class SmsPlanQuery extends PageParam {
    private String name;
}
