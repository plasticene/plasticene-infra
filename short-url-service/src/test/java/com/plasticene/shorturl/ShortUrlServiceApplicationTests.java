package com.plasticene.shorturl;

import com.plasticene.shorturl.service.ShortUrlService;
import com.plasticene.shorturl.service.UniqueCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShortUrlServiceApplicationTests {

    @Resource
    private ShortUrlService shortUrlService;
    @Resource
    private UniqueCodeService uniqueCodeService;


    void contextLoads() {
    }




    @Test
    public void generateCode() {
        uniqueCodeService.generateUniqueCode();
    }

}
