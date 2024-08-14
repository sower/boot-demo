package me.boot.cloud.alibaba.nacos.config;

import javax.annotation.Resource;
import me.boot.cloud.alibaba.nacos.config.bean.App;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ConfigAppTest {

    @Resource
    private App app;

    @Test
    public void test() {
        System.err.println(app);
    }

}
