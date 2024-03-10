package me.boot.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRecord {

    /**
     * 操作类型
     */
    String type() default "";

    /**
     * 业务编号
     */
    String bizNo() default "";

    String content() default "";

    String condition() default "";

    String unless() default "";

    // 是否异步执行
    boolean sync() default false;
}
