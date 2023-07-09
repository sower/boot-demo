package me.boot.web.aspect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义校验器
 *
 * @date 2023/02/04
 */
public class AnyOfValidator implements ConstraintValidator<AnyOf, String> {

    private boolean nullable;
    private boolean ignoreCase;
    private String[] legalValues;
    private String msg = null;

    @Override
    public void initialize(AnyOf constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.ignoreCase = constraintAnnotation.ignoreCase();

        Class<? extends Enum<?>>[] enumClasses = constraintAnnotation.enumClasses();
        String[] enumValues = Arrays.stream(enumClasses).map(this::getEnumValues)
            .flatMap(Collection::stream).toArray(String[]::new);

        this.legalValues = ArrayUtils.addAll(constraintAnnotation.values(), enumValues);

        this.msg = "Value only is " + Arrays.toString(legalValues);
    }

    private Set<String> getEnumValues(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(Enum::name)
            .collect(Collectors.toSet());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (context.getDefaultConstraintMessageTemplate().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.msg).addConstraintViolation();
        }

        if (value == null) {
            return this.nullable;
        }

        return
            this.ignoreCase ? StringUtils.containsAnyIgnoreCase(value, this.legalValues)
                : StringUtils.containsAny(value, this.legalValues);
    }
}
