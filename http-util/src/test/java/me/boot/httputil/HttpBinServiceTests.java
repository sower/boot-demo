package me.boot.httputil;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.httputil.service.HttpBinService;
import me.boot.httputil.util.CloseHttpUtil;
import me.boot.httputil.util.OkHttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HttpUtilApplication.class)
@Slf4j
class HttpBinServiceTests {

    @Resource
    private HttpBinService httpBinService;


    @Test
    public void testGet(){
        System.out.println(httpBinService.uuid());
    }


}
