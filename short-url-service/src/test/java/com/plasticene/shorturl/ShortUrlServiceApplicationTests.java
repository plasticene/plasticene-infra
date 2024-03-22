package com.plasticene.shorturl;

import com.plasticene.shorturl.entity.UrlLink;
import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.UniqueCodeService;
import com.plasticene.shorturl.service.impl.ShortUrlServiceImpl;
import com.plasticene.shorturl.utils.IpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShortUrlServiceApplicationTests {

    @Resource
    private ShortUrlServiceImpl shortUrlService;
    @Resource
    private UniqueCodeService uniqueCodeService;



    void contextLoads() {
    }




    @Test
    public void generateCode() {
        uniqueCodeService.generateUniqueCode();
    }

    @Test
    public void getUniqueCode() {
        uniqueCodeService.getUniqueCode();
    }

    @Test
    public void test() {
        IpUtils.getIpRegion("115.206.246.88");
    }

    @Test
    public void testBatchUpdate() {
        List<UrlLink> list = shortUrlService.list();
        list.forEach(urlLink -> {
            urlLink.setUpdateTime(new Date());
            urlLink.setUpdater(2l);
        });
        shortUrlService.updateBatchById(list);
    }

}
