package me.boot.easy.excel.handler;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.boot.easy.excel.controller.ExcelObject;
import me.boot.easy.excel.util.RequestContextUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;


public class ExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return ExcelObject.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
        ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException {
        ExcelObject excelObject = (ExcelObject) returnValue;
        HttpServletResponse downloadResponse = RequestContextUtil.getDownloadResponse(
            excelObject.getName() + excelObject.getType().getValue());
        mavContainer.setRequestHandled(true);
        ExcelWriteResolver.resolve(excelObject, downloadResponse.getOutputStream());
    }


}
