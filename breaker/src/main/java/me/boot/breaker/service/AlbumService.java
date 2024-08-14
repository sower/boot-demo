package me.boot.breaker.service;

import java.util.function.Supplier;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

/**
 * AlbumService
 *
 * @since 2024/06/16
 **/
@Service
public class AlbumService {

    @Resource
    private CircuitBreakerFactory circuitBreakerFactory;

    public void getAlbum() {
        runWithBreaker(this::findAlbumHasError);
        runWithBreaker(this::findAlbum);
        runWithBreaker(this::findAlbumLongTime);
    }

    private void runWithBreaker(Supplier<String> toRun ) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        String value = circuitBreaker.run(toRun, e -> "=> fallback");
        System.err.println(value);
    }


    private String findAlbum(){
        return this.getClass().getCanonicalName();
    }

    private String findAlbumHasError(){
        throw new RuntimeException("Runtime Exception");
    }

    @SneakyThrows
    private String findAlbumLongTime(){
        System.err.println("== sleep ==");
        // Thread.sleep(3000);
        return this.getClass().getSimpleName();
    }

}
