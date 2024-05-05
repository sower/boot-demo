package me.boot.easy.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单元格自适应
 *
 * @since 2023/09/29
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoCellSize {

    /**
     * 最大列宽
     */
    int maxColumnWidth() default 50;


    /**
     * 最大行高，默认自适应
     */
    int maxRowHeight() default -1;

}
