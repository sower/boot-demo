package me.boot.easy.excel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.boot.easy.excel.handler.ExcelReturnValueHandler;
import me.boot.easy.excel.handler.ExcelWriteHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


@AutoConfiguration
@RequiredArgsConstructor
public class EasyExcelPlusAutoConfiguration {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Bean
    @ConditionalOnMissingBean
    public ExcelWriteHandler excelWriteHandler() {
        return new ExcelWriteHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelReturnValueHandler excelReturnValueHandler(ExcelWriteHandler excelWriteHandler) {
        return new ExcelReturnValueHandler(excelWriteHandler);
    }

    /**
     * 追加处理器到springmvc
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(excelReturnValueHandler(excelWriteHandler()));
        newHandlers.addAll(Objects.requireNonNull(requestMappingHandlerAdapter
            .getReturnValueHandlers()));
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

}
