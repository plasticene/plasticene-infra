package com.plasticene.shorturl.utils;

import com.plasticene.shorturl.dto.IpRegion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/14 23:31
 */
@Slf4j
public class IpUtils {

    private static final String UNKNOWN_VALUE = "unknown";
    private static final String LOCALHOST_V4 = "127.0.0.1";
    private static final String LOCALHOST_V6 = "0:0:0:0:0:0:0:1";

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    private static final String IP_DATA_PATH = "/Users/shepherdmy/Desktop/ip2region.xdb";
    private static  byte[] contentBuff;

    static {
        try {
            // 从 dbPath 加载整个 xdb 到内存。
            contentBuff = Searcher.loadContentFromFile(IP_DATA_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取客户端ip地址
     * @param request
     * @return
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isNotEmpty(ip) && !UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader(X_REAL_IP);
        if (StringUtils.isNotEmpty(ip) && !UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            return ip;
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HTTP_CLIENT_IP);
        }

        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals(LOCALHOST_V6) ? LOCALHOST_V4 : ip;
    }

    /**
     * 根据ip查询归属地，固定格式：中国|0|浙江省|杭州市|电信
     * @param ip
     * @return
     */
    public static IpRegion getIpRegion(String ip) {
        Searcher searcher = null;
        IpRegion ipRegion = new IpRegion();
        try {
            searcher = Searcher.newWithBuffer(contentBuff);
            String region = searcher.search(ip);
            String[] info = StringUtils.split(region, "|");
            ipRegion.setCountry(info[0]);
            ipRegion.setArea(info[1]);
            ipRegion.setProvince(info[2]);
            ipRegion.setCity(info[3]);
            ipRegion.setIsp(info[4]);
        } catch (Exception e) {
            log.error("get ip region error: ", e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    log.error("close searcher error:", e);
                }
            }
        }
        return ipRegion;
    }


}
