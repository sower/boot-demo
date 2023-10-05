package me.boot.easy.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import me.boot.easy.excel.controller.ExcelResponse;
import me.boot.easy.excel.resolver.ExcelDropDownResolver;
import me.boot.easy.excel.strategy.DropDownHandler;
import me.boot.easy.excel.util.RequestContextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;


public class ExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return ExcelResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
        ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException {
        ExcelResponse excelResponse = (ExcelResponse) returnValue;
        HttpServletResponse downloadResponse = RequestContextUtil.getDownloadResponse(
            excelResponse.getName() + excelResponse.getType().getValue());
        mavContainer.setRequestHandled(true);

        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(downloadResponse.getOutputStream())
            .excelType(excelResponse.getType());
        if (CollectionUtils.isNotEmpty(excelResponse.getWriteHandlers())) {
            excelResponse.getWriteHandlers()
                .forEach(excelWriterBuilder::registerWriteHandler);
        }

        ExcelWriterSheetBuilder sheet = excelWriterBuilder
            .sheet(excelResponse.getSheetName());
        if (excelResponse.getHeadClass() != null) {
            sheet.head(excelResponse.getHeadClass());
            getWriteHandlers(excelResponse.getHeadClass())
                .forEach(sheet::registerWriteHandler);
        }
        if (excelResponse.getHead() != null) {
            sheet.head(excelResponse.getHead());
        }
        sheet.doWrite(excelResponse.getData());

    }


    public List<WriteHandler> getWriteHandlers(Class<?> headClass) {
        List<WriteHandler> writeHandlers = new LinkedList<>();
        DropDownHandler dropDownHandler = ExcelDropDownResolver.getDropDownHandler(headClass);
        writeHandlers.add(dropDownHandler);
        return writeHandlers.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


}
