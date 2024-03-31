package me.boot.web.mvc.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import me.boot.web.mvc.validation.SizePlus.List;


/**
 * Size增强版
 * <p>支持使用参数注入</p>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(List.class)
@Constraint(validatedBy = SizePlusValidator.class)
public @interface SizePlus {

    String message() default "{boot.validation.constraints.SizePlus.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String min() default "0";

    String max() default "100";

    boolean nullable() default false;

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        SizePlus[] value();
    }

}
