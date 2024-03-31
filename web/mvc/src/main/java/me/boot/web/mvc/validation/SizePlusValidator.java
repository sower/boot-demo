package me.boot.web.mvc.validation;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.context.SpringContextHolder;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * SizePlusValidator
 *
 * @since 2024/03/16
 **/
@Slf4j
public class SizePlusValidator implements ConstraintValidator<SizePlus, Object> {

    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

    private int min;
    private int max;

    private boolean nullable;

    private final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":",
        true);

    public void initialize(SizePlus parameters) {
        this.min = parseValue(parameters.min());
        this.max = parseValue(parameters.max());
        this.validateParameters();
        this.nullable = parameters.nullable();
    }

    public boolean isValid(Object input, ConstraintValidatorContext constraintValidatorContext) {
        ConstraintValidatorContextImpl context = (ConstraintValidatorContextImpl) constraintValidatorContext;
        context.addMessageParameter("min", this.min);
        context.addMessageParameter("max", this.max);

        int length;
        if (input == null) {
            return this.nullable;
        } else if (input instanceof CharSequence) {
            length = ((CharSequence) input).length();
        } else if (input instanceof Collection) {
            length = ((Collection<?>) input).size();
        } else if (input instanceof Map) {
            length = ((Map<?,?>) input).size();
        } else {
            log.error("Invalid type for SizePlus validator: {}", input.getClass());
            return false;
        }
        return length >= this.min && length <= this.max;
    }

    private int parseValue(String raw) {
        String value = this.helper.replacePlaceholders(raw, SpringContextHolder::getProperty);
        return Integer.parseInt(value);
    }

    private void validateParameters() {
        if (this.min < 0) {
            throw LOG.getMinCannotBeNegativeException();
        } else if (this.max < 0) {
            throw LOG.getMaxCannotBeNegativeException();
        } else if (this.max < this.min) {
            throw LOG.getLengthCannotBeNegativeException();
        }
    }
}
