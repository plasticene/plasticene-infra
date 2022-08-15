package com.plasticene.shorturl.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.entity.UniqueCode;
import com.plasticene.shorturl.service.UniqueCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/15 10:49
 *
 * 使用guava实现布隆过滤器
 */
@Slf4j
public class GuavaBloomFilter implements InitializingBean {

    @Resource
    private UniqueCodeService uniqueCodeService;

    /** 预计插入的数据 */
    private static Integer expectedInsertions = 100000000;
    /** 误判率 */
    private static Double fpp = 0.03;
    /** 布隆过滤器 */
    private BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
            expectedInsertions, fpp);

    @Override
    public void afterPropertiesSet() {
        initBloomFilter();
    }

    public void initBloomFilter() {
        log.info("===============初始化BloomFilter==============");
        long startTime = System.currentTimeMillis();
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
        bloomFilter.put(code);
    }

    public void addAll(Set<String> codes) {
        codes.forEach(code -> {
            bloomFilter.put(code);
        });
    }

    public Boolean isExist(String code) {
        return bloomFilter.mightContain(code);
    }

}
