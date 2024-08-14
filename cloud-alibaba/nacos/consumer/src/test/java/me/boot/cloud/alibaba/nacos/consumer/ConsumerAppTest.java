package me.boot.cloud.alibaba.nacos.consumer;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.cloud.alibaba.nacos.consumer.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class ConsumerAppTest {

    @Resource
    private DemoService demoService;

    @Test
    public void test() {
        demoService.echo("mate");
    }

}
