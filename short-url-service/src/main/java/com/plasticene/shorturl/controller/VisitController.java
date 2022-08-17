package com.plasticene.shorturl.controller;

import com.plasticene.boot.common.pojo.PageResult;
import com.plasticene.shorturl.dto.VisitRecordDTO;
import com.plasticene.shorturl.query.VisitQuery;
import com.plasticene.shorturl.service.VisitRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/17 13:42
 */
@RestController
@Api(tags = "访问记录管理")
@RequestMapping("/api/visit")
public class VisitController {

    @Resource
    private VisitRecordService visitRecordService;

    @GetMapping()
    @ApiOperation("访问记录列表")
    public PageResult<VisitRecordDTO> getList(VisitQuery query) {
        PageResult<VisitRecordDTO> pageResult = visitRecordService.getList(query);
        return pageResult;
    }


}
