package me.boot.httputil.interceptor;

import java.util.Arrays;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.annotation.LogRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * LogRecordAspect
 *
 * @since 2024/01/07
 **/
@Component
@Aspect
@Slf4j
public class LogRecordAspect {

    @Resource
    private DefaultListableBeanFactory defaultListableBeanFactory;


    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

//    @Pointcut("@annotation(me.boot.base.annotation.LogRecord)")
//    public void logRecord() {
//    }

    // auto record log
    @After("@annotation(logRecord)")
    public void doAround(JoinPoint joinPoint, LogRecord logRecord) {
        String record = getValueBySpEL(logRecord.content(), joinPoint);
        System.err.println(record);
    }


    public String getValueBySpEL(String spELString, JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        log.error("parameterNames: {}", Arrays.toString(parameterNames));
//        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
//        log.error("paramNames: {}", Arrays.toString(paramNames));
        Expression expression = parser.parseExpression(spELString);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(defaultListableBeanFactory));
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return expression.getValue(context).toString();
    }
}
