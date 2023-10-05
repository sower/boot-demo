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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDropDown {

    /**
     * 固定下拉内容
     */
    String[] values() default {};

    /**
     * 动态下拉内容
     */
    Class<? extends Enum<?>>[] enumClasses() default {};

    /**
     * 下拉框的起始行，默认head下一行
     */
    int startRow() default -1;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    int endRow() default 1000;
}
