package com.plasticene.shorturl.service.impl;

import com.plasticene.boot.common.utils.IdGenerator;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.entity.VisitRecord;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.VisitRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/14 23:02
 */
@Service
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
        String userAgent = request.getHeader(USER_AGENT);



    }
}
