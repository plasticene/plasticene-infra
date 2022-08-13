package com.plasticene.shorturl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.shorturl.dao.UniqueCodeDAO;
import com.plasticene.shorturl.entity.UniqueCode;
import com.plasticene.shorturl.service.UniqueCodeService;
import com.plasticene.shorturl.utils.RandomUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/12 14:26
 */
@Service
public class UniqueCodeServiceImpl extends ServiceImpl<UniqueCodeDAO, UniqueCode> implements UniqueCodeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IdGenerator idGenerator;


    private static final String UNIQUE_CODE_KEY = "short_url_unique_code";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateUniqueCode() {
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String s = RandomUtils.generateCode(6);
            codes.add(s);
        }
        stringRedisTemplate.opsForSet().add(UNIQUE_CODE_KEY, codes.toArray(new String[codes.size()]));
        List<UniqueCode> uniqueCodeList = new ArrayList<>();
        codes.forEach(code -> {
            UniqueCode uniqueCode = new UniqueCode();
            uniqueCode.setId(idGenerator.nextId());
            uniqueCode.setCode(code);
            uniqueCodeList.add(uniqueCode);
        });
        saveBatch(uniqueCodeList);
    }


    @Override
    public String getUniqueCode() {
        String code = stringRedisTemplate.opsForSet().pop(UNIQUE_CODE_KEY);
        return code;
    }

    @Override
    public Set<String> getUniqueCode(Integer size) {
        return null;
    }
}
