package com.plasticene.base.service;

import com.plasticene.base.dto.SmsPlanDTO;
import com.plasticene.base.param.SmsPlanParam;
import com.plasticene.base.query.SmsPlanQuery;
import com.plasticene.boot.common.pojo.PageResult;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/6 09:50
 */
public interface SmsPlanService {

    void addSmsPlan(SmsPlanParam param);

    PageResult<SmsPlanDTO> getList(SmsPlanQuery query);
}
