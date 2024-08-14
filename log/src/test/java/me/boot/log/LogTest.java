package me.boot.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * LogTest
 *
 * @since 2024/05/27
 **/
@Slf4j
public class LogTest {

    Marker marker = MarkerFactory.getMarker("Mate");

    @Test
    public void test() {
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn(marker, "warn");
        log.error("error");
    }

}
