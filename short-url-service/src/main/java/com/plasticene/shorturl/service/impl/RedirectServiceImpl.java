package com.plasticene.shorturl.service.impl;

import com.plasticene.boot.common.exception.BizException;
import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.service.RedirectService;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.VisitRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/16 10:03
 */
@Service
public class RedirectServiceImpl implements RedirectService {
    @Resource
    private ShortUrlService shortUrlService;
    @Resource
    private VisitRecordService visitRecordService;
    @Resource
    private ExecutorService executorService;
    @Override
    public void redirect(HttpServletRequest request, HttpServletResponse response, String uniqueCode) throws IOException {
        String longUrl = shortUrlService.getOriginUrl(uniqueCode);
        if (StringUtils.isBlank(longUrl)) {
            throw new BizException("短链接地址不存在");
        }
        executorService.execute(() -> {
            visitRecordService.addVisitRecord(request, uniqueCode);
        });
        response.sendRedirect(longUrl);



    }
}
