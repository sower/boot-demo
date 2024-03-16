package me.boot.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * ConsistentDateValidator
 *
 * @since 2024/03/16
 **/
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ConsistentValueValidator implements ConstraintValidator<ConsistentValue, Object[]> {

    private int beginIndex;

    private int endIndex;

    @Override
    public void initialize(ConsistentValue constraintAnnotation) {
        beginIndex = constraintAnnotation.beginIndex();
        endIndex = constraintAnnotation.endIndex();
    }

    @Override
    public boolean isValid(Object[] values, ConstraintValidatorContext context) {
        Object beginValue = ArrayUtils.get(values, beginIndex);
        Object endValue = ArrayUtils.get(values, endIndex);
        if (ObjectUtils.anyNull(beginValue, endValue)) {
            return true;
        }
        if (!(beginValue instanceof Comparable) || !ClassUtils.isAssignable(beginValue.getClass(),
            endValue.getClass())) {
            throw new IllegalArgumentException(
                "Illegal method signature, expected two parameters of type Comparable.");
        }

        return ((Comparable) beginValue).compareTo(endValue) < 0;
    }
}
