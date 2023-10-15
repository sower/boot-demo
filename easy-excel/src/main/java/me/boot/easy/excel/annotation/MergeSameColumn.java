package me.boot.easy.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description
 * @date 2023/09/29
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeSameColumn {

    /**
     * 需要合并行的下标，从0开始
     */
    int[] mergeRows() default {-1};

    /**
     * 开始合并列
     */
    int startColumn() default 0;

    /**
     * 结束合并列
     */
    int endColumn() default 0;

}
