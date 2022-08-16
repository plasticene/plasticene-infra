package com.plasticene.shorturl.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/16 10:01
 */
public interface RedirectService {

    void redirect(HttpServletRequest request, HttpServletResponse response, String uniqueCode) throws IOException;
}
