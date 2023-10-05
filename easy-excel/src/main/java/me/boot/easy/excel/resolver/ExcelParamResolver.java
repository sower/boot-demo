package me.boot.easy.excel.resolver;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import me.boot.base.context.SpringContextHolder;
import me.boot.easy.excel.annotation.ExcelParam;
import me.boot.easy.excel.util.RequestContextUtil;
import me.boot.easy.excel.validator.CompositeExcelValidator;
import me.boot.easy.excel.validator.ExcelValidException;
import me.boot.easy.excel.validator.ExcelValidator;
import me.boot.easy.excel.validator.ReadRow;
import me.boot.easy.excel.validator.ReadTable;
import me.boot.easy.excel.validator.errors.ExcelValidErrors;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public class ExcelParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        HttpServletRequest request = RequestContextUtil.getContextRequest();
        String contentType = request.getContentType();
        boolean isMultipart =
            contentType != null && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/");
        ExcelParam excelParam = methodParameter.getParameterAnnotation(ExcelParam.class);
        if (!isMultipart || excelParam == null) {
            return false;
        }
        ResolvableType param = ResolvableType.forMethodParameter(methodParameter);
        return ResolvableType.forClass(ReadTable.class).isAssignableFrom(param)
            || ResolvableType.forClass(List.class).isAssignableFrom(param);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
        ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
        WebDataBinderFactory webDataBinderFactory) throws Exception {
        ExcelParam excelParam = methodParameter.getParameterAnnotation(ExcelParam.class);
        MultipartRequest request = nativeWebRequest.getNativeRequest(MultipartRequest.class);
        MultipartFile file = request.getFile(excelParam.value());
        if (file == null) {
            if (excelParam.required()) {
                throw new MissingServletRequestPartException(excelParam.value());
            }
            return null;
        }

        ReadTable<Object> readTable = new ReadTable<>();
        ResolvableType[] generics = ResolvableType.forType(
            methodParameter.getGenericParameterType()).getGenerics();
        Class<?> component = generics[generics.length - 1].resolve();

        EasyExcel.read(file.getInputStream(), component, new AnalysisEventListener<>() {

            @Override
            public void invoke(Object data, AnalysisContext context) {
                Integer rowIndex = context.readRowHolder().getRowIndex();
                readTable.getRows().add(new ReadRow<>(rowIndex, data));
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                readTable.setExcelReadHeadProperty(
                    context.currentReadHolder().excelReadHeadProperty());
            }
        }).sheet().doRead();

        ExcelValidErrors errors = validateIfApplicable(methodParameter, readTable);
        if (errors.hasErrors() && !isBindExceptionRequired(methodParameter)) {
            throw new ExcelValidException("Valid param has errors", errors);
        }
        if (modelAndViewContainer != null) {
            modelAndViewContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + "excel", errors);
        }

        if (List.class.isAssignableFrom(methodParameter.getParameterType())) {
            return readTable.getRows().stream().map(ReadRow::getData).collect(Collectors.toList());
        }
        if (ReadTable.class == methodParameter.getParameterType()) {
            return readTable;
        }
        return null;
    }

    private ExcelValidErrors validateIfApplicable(MethodParameter parameter,
        ReadTable<Object> readTable) {
        Annotation[] annotations = parameter.getParameterAnnotations();

        boolean valid = false;
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return new ExcelValidErrors();
        }

        Class<?> headClazz = readTable.getExcelReadHeadProperty().getHeadClazz();

        List<ExcelValidator<Object>> validators =
            SpringContextHolder.getApplicationContext().getBeanProvider(ExcelValidator.class)
                .stream().filter(item -> {
                    Class<?> component = ResolvableType.forInstance(item).as(ExcelValidator.class)
                        .resolveGeneric(0);
                    return component == Object.class || component.isAssignableFrom(headClazz);
                }).map(item -> (ExcelValidator<Object>) item).collect(Collectors.toList());
        CompositeExcelValidator validator = new CompositeExcelValidator(validators);

        return validator.validate(readTable);
    }

    private boolean isBindExceptionRequired(MethodParameter parameter) {
        int nextIndex = parameter.getParameterIndex() + 1;
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        return paramTypes.length > nextIndex
            && ExcelValidErrors.class.isAssignableFrom(paramTypes[nextIndex]);
    }
}
