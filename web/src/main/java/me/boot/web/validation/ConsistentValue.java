package me.boot.web.validation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 连续值检查
 *
 * @since 2024/03/16
 **/
@Constraint(validatedBy = ConsistentValueValidator.class)
@Target({METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
@Documented
public @interface ConsistentValue {

    String message() default "{boot.validation.constraints.ConsistentTime.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int beginIndex() default 0;

    int endIndex() default 1;
}