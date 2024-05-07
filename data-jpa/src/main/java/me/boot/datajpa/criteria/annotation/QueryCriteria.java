package me.boot.datajpa.criteria.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.boot.datajpa.criteria.constant.QueryOperation;

/**
 * 查询归准
 *
 * <p> 字段级别优先级高于类级别 </p>
 **/
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCriteria {

    // 实体属性名，默认为字段名
    String name() default "";

    // 查询方式
    QueryOperation operation() default QueryOperation.EQUAL;

}
