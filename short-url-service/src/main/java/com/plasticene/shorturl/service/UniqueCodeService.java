package com.plasticene.shorturl.service;

import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/12 14:23
 */
public interface UniqueCodeService {

    void generateUniqueCode();

    void getUniqueCode();

    Set<String> getUniqueCode(Integer size);

}
