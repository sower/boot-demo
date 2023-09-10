package me.boot.web;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.httputil.service.HttpBinService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

@SpringBootTest
@Slf4j
class HttpBinServiceTests {

    @Resource
    @Lazy
    private HttpBinService httpBinService;


    @Test
    public void testGet(){
        System.out.println(httpBinService.uuid());
    }


}
