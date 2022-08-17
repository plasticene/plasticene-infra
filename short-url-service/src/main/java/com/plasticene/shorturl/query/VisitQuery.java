package com.plasticene.shorturl.query;

import com.plasticene.boot.common.pojo.PageParam;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/17 13:59
 */
@Data
public class VisitQuery extends PageParam {
    private String uniqueCode;
}
