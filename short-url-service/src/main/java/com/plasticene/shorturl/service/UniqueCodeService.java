package com.plasticene.shorturl.service;

import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.entity.UniqueCode;

import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/12 14:23
 */
public interface UniqueCodeService {

    void generateUniqueCode();

    String getUniqueCode();

    List<String> getUniqueCode(Integer size);

    PageResult<UniqueCode> getUnusedList(PageParam pageParam);

}
