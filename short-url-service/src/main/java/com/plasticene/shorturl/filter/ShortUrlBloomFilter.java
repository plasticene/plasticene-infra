package com.plasticene.shorturl.filter;

import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.entity.UniqueCode;
import com.plasticene.shorturl.service.UniqueCodeService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/15 09:46
 *
 * 基于redisson实现布隆过滤器
 */
@Component
@Slf4j
public class ShortUrlBloomFilter implements InitializingBean {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UniqueCodeService uniqueCodeService;
    @Resource
    private RBloomFilter bloomFilter;

    @Value("${bloom-init: false}")
    private Boolean BloomInit;

    private static final String UNIQUE_CODE_BLOOM_FILTER_KEY = "bf:unique-code";

    @Override
    public void afterPropertiesSet() {
        initBloomFilter();
    }

    public void initBloomFilter() {
        if (!BloomInit) {
            return;
        }
        log.info("===============初始化BloomFilter==============");
        long startTime = System.currentTimeMillis();
        // 删除布隆过滤器key
        redisTemplate.delete(UNIQUE_CODE_BLOOM_FILTER_KEY);
        PageParam pageParam = new PageParam(1, 2000);
        PageResult<UniqueCode> pageResult = uniqueCodeService.getUnusedList(pageParam);
        if (!CollectionUtils.isEmpty(pageResult.getList())) {
            Set<String> codes = pageResult.getList().parallelStream().map(UniqueCode::getCode).collect(Collectors.toSet());
            addAll(codes);
        }
        Long total = pageResult.getTotal();
        Long pages = pageResult.getPages();
        if (pages > 1) {
            for(int i = 2; i <= pages; i++) {
                PageParam param = new PageParam(i, 2000);
                PageResult<UniqueCode> result = uniqueCodeService.getUnusedList(param);
                Set<String> codes = result.getList().parallelStream().map(UniqueCode::getCode).collect(Collectors.toSet());
                addAll(codes);
            }
        }
        long costTime = System.currentTimeMillis() - startTime;
        log.info("===============初始化BloomFilter完成,costTime:[{}],Count:[{}]==============", costTime, total);
    }

    public void add(String code) {
        bloomFilter.add(code);
    }

    // 这里使用循环添加key是低效的
    public void addAll(Set<String> codes) {
        codes.forEach(code -> {
            bloomFilter.add(code);
        });
    }

    public Boolean isExist(String code) {
        return bloomFilter.contains(code);
    }

}
