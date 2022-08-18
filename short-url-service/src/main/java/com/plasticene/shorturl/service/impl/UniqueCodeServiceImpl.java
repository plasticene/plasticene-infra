package com.plasticene.shorturl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.shorturl.constant.CommonConstant;
import com.plasticene.shorturl.dao.UniqueCodeDAO;
import com.plasticene.shorturl.entity.UniqueCode;
import com.plasticene.shorturl.filter.ShortUrlBloomFilter;
import com.plasticene.shorturl.service.UniqueCodeService;
import com.plasticene.shorturl.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/12 14:26
 */
@Service
@Slf4j
public class UniqueCodeServiceImpl extends ServiceImpl<UniqueCodeDAO, UniqueCode> implements UniqueCodeService {
    @Resource
    private UniqueCodeDAO uniqueCodeDAO;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private ExecutorService executorService;
    @Resource
    private ShortUrlBloomFilter shortUrlBloomFilter;

    @Value("${unique-code.max-size}")
    private Integer maxSize;
    @Value("${unique-code.min-size}")
    private Integer minSize;


    private static final String UNIQUE_CODE_KEY = "short_url_unique_code";

    @Override
    public String getUniqueCode() {
        String code = stringRedisTemplate.opsForSet().pop(UNIQUE_CODE_KEY);
        asyncGenerateUniqueCode();
        return code;
    }

    @Override
    public List<String> getUniqueCode(Integer size) {
        List<String> codes = stringRedisTemplate.opsForSet().pop(UNIQUE_CODE_KEY, size);
        asyncGenerateUniqueCode();
        return codes;
    }

    @Override
    public PageResult<UniqueCode> getUnusedList(PageParam pageParam) {
        LambdaQueryWrapper<UniqueCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UniqueCode::getStatus, CommonConstant.CODE_NOT_USE);
        queryWrapper.select(UniqueCode::getCode);
        PageResult<UniqueCode> pageResult = uniqueCodeDAO.selectPage(pageParam, queryWrapper);
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateUniqueCode() {
        log.info("==========开始生成压缩码===========");
        long startTime = System.currentTimeMillis();
        Set<String> codes = new HashSet<>();
        Set<String> existCodes = new HashSet<>();
        for (int i = 0; i < maxSize; i++) {
            String code = RandomUtils.generateCode(6);
            Boolean exist = shortUrlBloomFilter.isExist(code);
            if (exist) {
                existCodes.add(code);
            } else {
                codes.add(code);
            }
        }
        if (!CollectionUtils.isEmpty(existCodes)) {
            log.info("=========以下压缩码已存在：{}", existCodes);
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
        long costTime = System.currentTimeMillis() - startTime;
        log.info("===============结束生成压缩码, costTime:[{}],Count:[{}]==============", costTime, codes.size());
    }

    public void asyncGenerateUniqueCode() {
        Long size = stringRedisTemplate.opsForSet().size(UNIQUE_CODE_KEY);
        // 如果剩余压缩码数量小于最小数量，异步生成压缩码
        if (size < minSize) {
            executorService.execute(() -> {
                generateUniqueCode();
            });
        }

    }
}
