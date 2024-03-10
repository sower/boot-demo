package me.boot.web.aspect;

import java.util.Arrays;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.boot.web.utils.RequestContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * AOP
 *
 * @date 2022/09/12
 */
@Component
@Aspect
@Slf4j
public class WebLogAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void requestLog() {
    }

    @Pointcut("@within(me.boot.base.annotation.LogMethod) || @annotation(me.boot.base.annotation.LogMethod)")
    public void methodLog() {
    }

    @Before("requestLog()")
    public void requestBefore() {
        HttpServletRequest request = RequestContextUtils.getContextRequest();
        log.info(
            "Received {} - {} request {}",
            request.getRemoteAddr(),
            request.getMethod(),
            ServletUriComponentsBuilder.fromRequest(request).encode().build());
    }

    /*前置通知*/
    @Before("requestLog() || methodLog()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info(
            "Ready into {} . {} - ARGS: (name) {} - (value) {}",
            signature.getDeclaringTypeName(),
            signature.getName(),
            signature.getParameterNames(),
            Arrays.toString(joinPoint.getArgs()));
    }

    // 后置通知
    @AfterReturning(value = "requestLog() || methodLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.info("Exit from {} - {}", joinPoint.getTarget(), joinPoint.getSignature().getName());
    }

    // 环绕通知
    @Around("requestLog() || methodLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch(proceedingJoinPoint.toShortString());
        stopWatch.start();
        Object obj = null;
        try {
            // 执行当前目标方法
            obj = proceedingJoinPoint.proceed();
            return obj;
        } finally {
            stopWatch.stop();
            String methodName = proceedingJoinPoint.getSignature().getName();
            log.info(
                "{} - {} complete, it takes {}ms",
                proceedingJoinPoint.getTarget(),
                methodName,
                stopWatch.getLastTaskTimeMillis());
            log.info(
                "{} return result -> {}",
                methodName,
                StringUtils.abbreviate(Objects.toString(obj) , 1000));
        }
    }

    // 最终通知
    @After("requestLog()")
    public void doAfter() {
        log.info("last notify");
    }

    // 异常通知
    @AfterThrowing(value = "requestLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info(
            "{} - {} execute error {}",
            joinPoint.getTarget(),
            joinPoint.getSignature().getName(),
            e.getMessage());
    }
}
