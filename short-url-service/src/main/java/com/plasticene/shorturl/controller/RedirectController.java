package com.plasticene.shorturl.controller;

import com.plasticene.boot.common.exception.BizException;
import com.plasticene.shorturl.service.RedirectService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/16 09:57
 */
@RestController
@Api(tags = "重定向接口管理")
@RequestMapping("/x")
public class RedirectController {

    @Resource
    private RedirectService redirectService;

    @GetMapping("/{uniqueCode}")
    public void redirect(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("uniqueCode") String uniqueCode) throws IOException {

        redirectService.redirect(request, response, uniqueCode);
    }


}

