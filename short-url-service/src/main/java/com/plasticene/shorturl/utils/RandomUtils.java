package com.plasticene.shorturl.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/11 09:46
 */
public class RandomUtils {

    /**
     * 在进制表示中的字符集合, 这里的62个字符可以随机打乱，降低生成压缩码的规律性
     */
    final static char[] digits = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     * 10进制转62进制
     * @param seq
     * @return
     */
    public static String to62RadixString(long seq) {
        StringBuilder s = new StringBuilder();
        while (true) {
            int remainder = (int) (seq % 62);
            s.append(digits[remainder]);
            seq = seq / 62;
            if (seq == 0) {
                break;
            }
        }
        return s.reverse().toString();
    }


    /**
     * 这个方法是在62进制压缩码上再生成随机场组合，防止被别人通过找到字符规律使用别人的压缩码
     * @param seq
     * @return
     */
    public static String generateCode(long seq, long randomLength) {
        String s = to62RadixString(seq);
        Random random = new Random(System.currentTimeMillis());
        int count = 0;
        StringBuilder randomStr = new StringBuilder();
        while(count < randomLength) {
            int index = random.nextInt(Integer.MAX_VALUE) % digits.length;
            randomStr.append(digits[index]);
            count++;
        }
        return s + randomStr.toString();
    }

    // 生成随机码
    public static String generateCode(long length) {
        Random random = new Random();
        int count = 0;
        StringBuilder randomStr = new StringBuilder();
        while(count < length) {
            int num = random.nextInt(Integer.MAX_VALUE);
            int index = num % digits.length;
            randomStr.append(digits[index]);
            count++;
        }
        return randomStr.toString();
    }

    public static void main(String[] args) throws InterruptedException {
//        IdGenerator snowFlake = new IdGenerator(2, 3);
//        for (int i = 0; i < 10; i++) {
//            long seq = snowFlake.nextId();
//            System.out.println(seq);
//            String s1 = to62RadixString(seq);
//            System.out.println(s1);
//        }
//        long seq = 62l*62l*62l*62l*62l*62l*62l;
//        System.out.println(seq);
//        String s = to62RadixString( seq);
//        System.out.println(s);
//        String s1 = encode62(seq);
//        System.out.println(s1);
        Set<String> codes = new HashSet<>();
        for(int i=0;i<1000;i++){
            codes.add(generateCode(6));
        }
        System.out.println(codes.size());


    }

}
