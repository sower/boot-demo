package me.boot.easy.excel.handler;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.boot.easy.excel.controller.ExcelResponse;
import me.boot.easy.excel.util.RequestContextUtil;
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
        ExcelWriteResolver.write(excelResponse, downloadResponse.getOutputStream());
    }


}
