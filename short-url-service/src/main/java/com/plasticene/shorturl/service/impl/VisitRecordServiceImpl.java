package com.plasticene.shorturl.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.entity.VisitRecord;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.VisitRecordService;
import com.plasticene.shorturl.utils.IpUtils;
import eu.bitwalker.useragentutils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/14 23:02
 */
@Service
@Slf4j
public class VisitRecordServiceImpl implements VisitRecordService {

    private static final String USER_AGENT = "User-Agent";

    @Resource
    private ShortUrlService shortUrlService;
    @Resource
    private IdGenerator idGenerator;


    @Override
    public void addVisitRecord(HttpServletRequest request, UrlLink urlLink) {
        VisitRecord visitRecord = new VisitRecord();
        long id = idGenerator.nextId();
        visitRecord.setId(id);
        visitRecord.setUrlLinkId(urlLink.getId());
        visitRecord.setUniqueCode(urlLink.getUniqueCode());
        String agent = request.getHeader(USER_AGENT);
        String clientIp = IpUtils.getRemoteHost(request);
        visitRecord.setUserAgent(agent);
        visitRecord.setClientIp(clientIp);
        // 身份唯一标识,算法:SHA-1(客户端IP + '-' + UserAgent)
        String clientId = DigestUtil.sha1Hex(clientIp + "&" + agent);
        visitRecord.setClientId(clientId);

        // 解析User-Agent
        if (StringUtils.isNotBlank(agent)) {
            try {
                UserAgent userAgent = UserAgent.parseUserAgentString(agent);
                OperatingSystem operatingSystem = userAgent.getOperatingSystem();
                // 操作系统
                Optional.ofNullable(operatingSystem).ifPresent(x -> {
                    visitRecord.setOsType(x.getName());
                    visitRecord.setOsVersion(x.getName());
//                    visitRecord.setDeviceType(x.getDeviceType() == null ? null : x.getDeviceType().getName());
                    Optional.ofNullable(x.getDeviceType()).ifPresent(deviceType -> visitRecord.setDeviceType(deviceType.getName()));
                });
                // 浏览器类型
                Browser browser = userAgent.getBrowser();
                Optional.ofNullable(browser).ifPresent(x -> visitRecord.setBrowserType(x.getGroup().getName()));
                // 浏览器版本
                Version browserVersion = userAgent.getBrowserVersion();
                Optional.ofNullable(browserVersion).ifPresent(x -> visitRecord.setBrowserVersion(x.getVersion()));
            } catch (Exception e) {
                log.error("解析TransformEvent中的UserAgent异常,事件内容:", e);
            }
        }



    }
}
