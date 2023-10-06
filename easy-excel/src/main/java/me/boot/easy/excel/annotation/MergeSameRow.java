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
public @interface MergeSameRow {

    /**
     * 需要合并列，从0开始
     */
    int[] mergeColumns();

    /**
     * 开始合并行
     */
    int startRow() default 0;

    /**
     * 结束合并行
     */
    int endRow() default 0;

}
