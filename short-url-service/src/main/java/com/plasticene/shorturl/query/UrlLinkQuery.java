package com.plasticene.shorturl.query;

import com.plasticene.boot.common.pojo.PageParam;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/17 17:07
 */
@Data
public class UrlLinkQuery extends PageParam {
    private String uniqueCode;
}
