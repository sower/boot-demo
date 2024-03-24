package me.boot.base.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.PropertyResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * SpelParser
 *
 * @since 2024/03/20
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class SpelParser {

    //    private final BeanResolver beanResolver = ;
    private final DefaultListableBeanFactory defaultListableBeanFactory;
    private final PropertyResolver propertyResolver;
    private final PropertyAccessor propertyAccessor = new BeanExpressionContextAccessor();

    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();


    public String getValueBySpel(@NonNull String spelString, JoinPoint joinPoint) {
        return getValueBySpel(spelString, joinPoint, String.class);
    }


    public <T> T getValueBySpel(@NonNull String spelString, JoinPoint joinPoint,
        Class<T> resultType) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        log.debug("parameterNames: {}", Arrays.toString(parameterNames));
        Object[] args = joinPoint.getArgs();
        Map<String, Object> objectMap = new HashMap<>();
//        objectMap.put("root", methodSignature);
        objectMap.put("target", joinPoint.getTarget());
        objectMap.put("method", methodSignature.getMethod());
        objectMap.put("args", args);
        for (int i = 0; i < args.length; i++) {
            objectMap.put(parameterNames[i], args[i]);
        }
        return getValueBySpel(spelString, resultType, objectMap);
    }


    public String getValueBySpel(@NonNull String spelString) {
        return getValueBySpel(spelString, Collections.emptyMap());
    }

    public String getValueBySpel(@NonNull String spelString,
        Map<String, Object> varMap) {
        return getValueBySpel(spelString, String.class, varMap);
    }

    public <T> T getValueBySpel(@NonNull String spelString, Class<T> resultType,
        Map<String, Object> varMap) {
        String placeholders = propertyResolver.resolvePlaceholders(spelString);
        Expression expression = parseExpression(placeholders);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(defaultListableBeanFactory));
        context.addPropertyAccessor(propertyAccessor);
        if (MapUtils.isNotEmpty(varMap)) {
            context.setVariables(varMap);
        }
        return expression.getValue(context, resultType);
    }

    private Expression parseExpression(String spelString) {
        if (spelString.startsWith("#") && !spelString.startsWith("#{")) {
            return parser.parseExpression(spelString);
        }
        return parser.parseExpression(spelString, ParserContext.TEMPLATE_EXPRESSION);
    }
}
