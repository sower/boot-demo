package me.boot.web.mvc.validation;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

/**
 * 自定义校验器
 *
 * @date 2023/02/04
 */
public class AnyOfValidator implements ConstraintValidator<AnyOf, String> {

    private boolean nullable;
    private boolean ignoreCase;
    private String[] legalValues;

    @Override
    public void initialize(AnyOf constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.ignoreCase = constraintAnnotation.ignoreCase();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        String[] enumNames = Arrays.stream(enumClass.getEnumConstants()).map(Enum::name)
            .toArray(String[]::new);

        this.legalValues = ArrayUtils.addAll(constraintAnnotation.values(), enumNames);
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext validatorContext) {
        ConstraintValidatorContextImpl context = (ConstraintValidatorContextImpl) validatorContext;
        context.addMessageParameter("values", this.legalValues);

        if (value == null) {
            return this.nullable;
        }

        return this.ignoreCase ? StringUtils.containsAnyIgnoreCase(value, this.legalValues)
            : StringUtils.containsAny(value, this.legalValues);
    }
}
