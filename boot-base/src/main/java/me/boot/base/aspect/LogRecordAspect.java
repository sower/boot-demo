package me.boot.base.aspect;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.annotation.LogRecord;
import me.boot.base.util.SpelParser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * LogRecordAspect
 *
 * @since 2024/01/07
 **/
@Component
@Aspect
@Slf4j
public class LogRecordAspect {

    @Resource
    private SpelParser spelParser;


    // auto record log
    @AfterReturning("@annotation(logRecord)")
    public void doAround(JoinPoint joinPoint, LogRecord logRecord) {
        String record = spelParser.parseValue(logRecord.content(), joinPoint);
        System.err.println(record);
    }


}
