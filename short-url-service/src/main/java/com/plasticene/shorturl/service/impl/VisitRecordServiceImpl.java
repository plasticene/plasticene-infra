package com.plasticene.shorturl.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plasticene.boot.common.pojo.PageParam;
import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.plasticene.shorturl.dao.VisitRecordDAO;
import com.plasticene.shorturl.dto.IpRegion;
import com.plasticene.shorturl.dto.VisitRecordDTO;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.entity.VisitRecord;
import com.plasticene.shorturl.query.VisitQuery;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.VisitRecordService;
import com.plasticene.shorturl.utils.IpUtils;
import eu.bitwalker.useragentutils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private VisitRecordDAO visitRecordDAO;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private ShortUrlService shortUrlService;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVisitRecord(HttpServletRequest request, String uniqueCode) {
        VisitRecord visitRecord = new VisitRecord();
        long id = idGenerator.nextId();
        visitRecord.setId(id);
        UrlLink urlLink = shortUrlService.getUrlLink(uniqueCode);
        visitRecord.setUrlLinkId(urlLink.getId());
        visitRecord.setUniqueCode(urlLink.getUniqueCode());
        visitRecord.setVisitTime(new Date());
        String agent = request.getHeader(USER_AGENT);
        String clientIp = IpUtils.getRemoteHost(request);
        IpRegion ipRegion = IpUtils.getIpRegion(clientIp);
        visitRecord.setUserAgent(agent);
        visitRecord.setClientIp(clientIp);
        // 身份唯一标识,算法:SHA-1(客户端IP + '-' + UserAgent)
        String clientId = DigestUtil.sha1Hex(clientIp + "&" + agent);
        visitRecord.setClientId(clientId);
        visitRecord.setCountry(ipRegion.getCountry());
        visitRecord.setProvince(ipRegion.getProvince());
        visitRecord.setCity(ipRegion.getCity());
        visitRecord.setIsp(ipRegion.getIsp());

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
                log.error("解析UserAgent异常,事件内容:", e);
            }
        }
        visitRecordDAO.insert(visitRecord);
    }

    @Override
    public PageResult<VisitRecordDTO> getList(VisitQuery query) {
        LambdaQueryWrapperX<VisitRecord> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(VisitRecord::getUniqueCode, query.getUniqueCode());
        PageParam pageParam = new PageParam(query.getPageNo(), query.getPageSize());
        PageResult<VisitRecord> pageResult = visitRecordDAO.selectPage(pageParam, queryWrapper);
        List<VisitRecordDTO> visitRecordDTOList = toVisitRecordDTOList(pageResult.getList());
        PageResult<VisitRecordDTO> result = new PageResult<>();
        result.setList(visitRecordDTOList);
        result.setTotal(pageResult.getTotal());
        result.setPages(pageResult.getPages());
        return result;
    }

    List<VisitRecordDTO> toVisitRecordDTOList(List<VisitRecord> visitRecords) {
        List<VisitRecordDTO> visitRecordDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(visitRecords)) {
            return visitRecordDTOList;
        }
        visitRecords.forEach(visitRecord -> {
            VisitRecordDTO visitRecordDTO = PtcBeanUtils.copy(visitRecord, VisitRecordDTO.class);
            visitRecordDTOList.add(visitRecordDTO);
        });
        return visitRecordDTOList;
    }
}
