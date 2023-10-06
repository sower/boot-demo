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
public @interface ExcelAutoFilter {

    int firstRow() default 0;

    int lastRow() default 0;

    int firstCol() default 0;

    int lastCol() default 0;

}
