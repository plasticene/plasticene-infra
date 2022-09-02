package com.plasticene.base.utils;

import com.plasticene.base.entity.SmsTemplate;
import org.springframework.lang.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/9/2 16:50
 */
public class PlaceHolderUtils {

    private static final String ALIYUN_PLACEHOLDER_PREFIX = "${";

    private static final String ALIYUN_PLACEHOLDER_SUFFIX = "}";

    private static final Map<String, WeakReference<Pattern>> PATTERN_MAP = new ConcurrentHashMap<>(64);


    public static String replacePlaceHolder(SmsTemplate smsTemplate, Map<String, Object>params) {
        String content = smsTemplate.getContent();
        Set<String> paramSet = smsTemplate.getParams();
        for(String key : paramSet) {

        }
        return content;


    }

    /**
     * 替换包含占位符的字符串中的占位符
     * 占位符 前缀 + key + 后缀,例如 {url}
     *
     * @param contentWithPlaceHolder 包含占位符的字符串
     * @param map                    占位符-替换内容的集合
     * @param placeHolderRegex       占位符正则
     * @param keyRegex               占位符key正则
     * @return 替换后的内容
     */
    public static String replacePlaceHolder(String contentWithPlaceHolder, Map<String, Object> map, String placeHolderRegex, String keyRegex) {
        Pattern pattern = getCachedPattern(placeHolderRegex);
        Matcher matcher = pattern.matcher(contentWithPlaceHolder);
        //循环，字符串中有多少个符合的，就循环多少次
        while (matcher.find()) {
            //每一个符合正则的字符串
            String subStr = matcher.group();
            Pattern p = getCachedPattern(keyRegex);
            Matcher m = p.matcher(subStr);
            if (m.find()) {
                //占位符key
                String key = m.group();
                Object value = map.get(key);
                if (value != null) {
                    contentWithPlaceHolder = contentWithPlaceHolder.replace(subStr, value.toString());
                }
            }
        }
        return contentWithPlaceHolder;
    }


    /**
     * 根据指定正则从指定字符串中获取占位符key集合
     *
     * @param contentWithPlaceHolder 包含占位符的字符串
     * @param placeHolderRegex       占位符正则
     * @param keyRegex               占位符key正则
     * @return 占位符key集合
     */
    @NonNull
    public static Set<String> getPlaceHolderKeySet(String contentWithPlaceHolder, String placeHolderRegex, String keyRegex) {
        Set<String> placeHolderKeySet = new HashSet<>();
        Matcher matcher = getMatcher(contentWithPlaceHolder, placeHolderRegex);
        //循环，字符串中有多少个符合的，就循环多少次
        while (matcher.find()) {
            //每一个符合正则的字符串
            String subStr = matcher.group();
            Pattern p = getCachedPattern(keyRegex);
            Matcher m = p.matcher(subStr);
            if (m.find()) {
                placeHolderKeySet.add(m.group());
            }
        }
        return placeHolderKeySet;
    }

    /**
     * 从缓存中获取Pattern
     *
     * @param regex 正则表达式
     * @return Pattern对象
     */
    private static Pattern getCachedPattern(String regex) {
        WeakReference<Pattern> weakReference = PATTERN_MAP.get(regex);
        if (weakReference == null) {
            Pattern pattern = Pattern.compile(regex);
            PATTERN_MAP.put(regex, new WeakReference<>(pattern));
            return pattern;
        }
        return weakReference.get();
    }

    private static Matcher getMatcher(String contentWithPlaceHolder, String regex) {
        return getCachedPattern(regex).matcher(contentWithPlaceHolder);
    }
}
