package me.boot.easy.excel;


import com.alibaba.excel.util.ListUtils;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.boot.easy.excel.handler.ExcelReturnValueHandler;
import me.boot.easy.excel.resolver.ExcelParamResolver;
import me.boot.easy.excel.resolver.ExcelValidErrorsResolver;
import me.boot.easy.excel.validator.DefaultExcelValidator;
import me.boot.easy.excel.validator.ExcelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


@RequiredArgsConstructor
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class EasyExcelPlusAutoConfiguration {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Bean
    @ConditionalOnMissingBean
    public ExcelReturnValueHandler excelReturnValueHandler() {
        return new ExcelReturnValueHandler();
    }

    /**
     * 追加处理器到springmvc
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> newHandlers = ListUtils.newArrayList(
            excelReturnValueHandler());
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
            .getReturnValueHandlers();
        newHandlers.addAll(returnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

    @Bean
    @ConditionalOnBean(Validator.class)
    @ConditionalOnProperty(prefix = "easyexcel.validator.default", name = "enable", havingValue = "true", matchIfMissing = true)
    public ExcelValidator<Object> defaultExcelValidator(Validator validator) {
        return new DefaultExcelValidator(validator);
    }

    @Bean
    public ExcelParamResolver excelDataResolver() {
        return new ExcelParamResolver();
    }

    /**
     * 设置解析器，保证自定义解析器优先级最高
     *
     * @param adapter
     */
    @Autowired
    public void setArgumentResolvers(RequestMappingHandlerAdapter adapter) {
        List<HandlerMethodArgumentResolver> argumentResolvers = ListUtils.newArrayList(
            excelDataResolver(), new ExcelValidErrorsResolver()
        );
        argumentResolvers.addAll(adapter.getArgumentResolvers());
        adapter.setArgumentResolvers(argumentResolvers);
    }

}
