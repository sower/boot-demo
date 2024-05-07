package me.boot.data.redisson.aop;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.annotation.DistributedLock;
import me.boot.base.parser.SpelParser;
import me.boot.data.redisson.util.RedissonUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.stereotype.Component;

/**
 * 分布式锁切切面
 *
 * @since 2024/01/07
 **/
@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

    @Resource
    private SpelParser spelParser;

    @Around(value = "@annotation(distributedLock)")
    public Object doAround(ProceedingJoinPoint joinPoint, DistributedLock distributedLock)
        throws Throwable {

        // 解析注解值
        long waitTime = parseTime(distributedLock.waitTime());
        long leaseTime = parseTime(distributedLock.leaseTime());
        String key = getKey(joinPoint, distributedLock.key());

        // 获取锁
        RLock lock = RedissonUtils.getLock(key);
        boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        if (!isLocked) {
            log.warn("Failed to get lock {}", key);
            if (distributedLock.ignoreFailed()) {
                return null;
            }
            throw new RuntimeException("Failed to get DistributedLock");
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    private String getKey(JoinPoint joinPoint, String key) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        String name = signature.getName();
        String args = Arrays.toString(joinPoint.getArgs());
        log.info("Ready try lock method {} . {} - ARGS: (name) {} - (value) {}", declaringTypeName,
            name, signature.getParameterNames(), args);
        if (StringUtils.isBlank(key)) {
            return String.join(".", declaringTypeName, name, args);
        }
        return spelParser.parseValue(key, joinPoint);
    }

    private long parseTime(String input) {
        if (StringUtils.isBlank(input)) {
            return 0;
        }
        String value = spelParser.parseValue(input);
        return DurationStyle.detectAndParse(value).toMillis();
    }

}
