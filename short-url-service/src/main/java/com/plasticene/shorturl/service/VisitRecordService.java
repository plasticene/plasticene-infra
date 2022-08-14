package com.plasticene.shorturl.service;

import com.plasticene.shorturl.entity.UrlLink;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/14 22:59
 */
public interface VisitRecordService {

    void addVisitRecord(HttpServletRequest request, UrlLink urlLink);
}
