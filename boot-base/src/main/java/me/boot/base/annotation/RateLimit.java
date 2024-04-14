package me.boot.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.boot.base.annotation.RateLimit.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(List.class)
public @interface RateLimit {

    int NOT_LIMITED = 0;

    /**
     * 唯一键，默认为方法名
     */
    String key() default StringUtils.EMPTY;

    /**
     * qps
     */
    @AliasFor("qps")
    long value() default NOT_LIMITED;

    /**
     * qps
     */
    @AliasFor("value")
    long qps() default NOT_LIMITED;

    /**
     * 超时时长
     */
    String timeout() default "3s";

    Strategy strategy() default Strategy.IP;

    enum Strategy {
        IP,
        TOTAL,
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        RateLimit[] value();
    }
}
