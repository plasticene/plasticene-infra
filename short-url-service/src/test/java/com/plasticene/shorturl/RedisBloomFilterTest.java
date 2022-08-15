package com.plasticene.shorturl;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/15 14:02
 */
public class RedisBloomFilterTest {
    /** 预计插入的数据 */
    private static Integer expectedInsertions = 10;
    /** 误判率 */
    private static Double fpp = 0.01;

    public static void main(String[] args) {
        // Redis连接配置，无密码
        Config config = new Config();
        config.useSingleServer().setAddress("redis://110.42.224.58:6379").setDatabase(8);

        // config.useSingleServer().setPassword("123456");

        // 初始化布隆过滤器
        RedissonClient client = Redisson.create(config);
        RBloomFilter<Object> bloomFilter = client.getBloomFilter("user");
        bloomFilter.tryInit(expectedInsertions, fpp);

        System.out.println(1);
        // foreach add data
        for (Integer i = 0; i < 100000000; i++) {
            bloomFilter.add(i);
        }

        System.out.println(2);

        // 统计元素
        int count = 0;
        for (int i = expectedInsertions; i < expectedInsertions*2; i++) {
            if (bloomFilter.contains(i)) {
                count++;
            }
        }
        System.out.println("误判次数" + count);
        System.out.println(3);

    }
}
