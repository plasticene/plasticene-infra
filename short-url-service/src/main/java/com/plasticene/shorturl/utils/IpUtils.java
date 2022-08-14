package com.plasticene.shorturl.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/14 23:31
 */
public class IpUtils {

    private static final String UNKNOWN_VALUE = "unknown";
    private static final String LOCALHOST_V0 = "0.0.0.0";
    private static final String LOCALHOST_V4 = "127.0.0.1";
    private static final String LOCALHOST_V6 = "0:0:0:0:0:0:0:1";

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";


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

}
