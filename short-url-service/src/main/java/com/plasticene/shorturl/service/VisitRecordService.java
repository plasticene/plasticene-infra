package com.plasticene.shorturl.service;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.dto.VisitRecordDTO;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.query.VisitQuery;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/14 22:59
 */
public interface VisitRecordService {

    void addVisitRecord(HttpServletRequest request, String uniqueCode);

    PageResult<VisitRecordDTO> getList(VisitQuery query);
}
