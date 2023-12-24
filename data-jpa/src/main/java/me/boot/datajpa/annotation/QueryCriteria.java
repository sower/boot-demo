package me.boot.datajpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.boot.base.constant.Operation;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCriteria {

    // 基本对象的属性名
    String name() default "";

    // 查询方式
    Operation operation() default Operation.EQUAL;

}
