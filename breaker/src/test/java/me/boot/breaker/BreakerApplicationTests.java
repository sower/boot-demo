package me.boot.breaker;

import javax.annotation.Resource;
import me.boot.breaker.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BreakerApplicationTests {

    @Resource
    private AlbumService albumService;

    @Test
    void contextLoads() {
        albumService.getAlbum();
    }

}
