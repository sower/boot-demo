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
public @interface AutoCellSize {

    /**
     * 冻结的行数 默认为表头最后一行
     */
    int maxColumnWidth() default 50;

    // 冻结的列数
    int maxRowHeight() default -1;

}
