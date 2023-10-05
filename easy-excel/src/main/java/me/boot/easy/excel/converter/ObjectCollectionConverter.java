package me.boot.easy.excel.converter;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.boot.easy.excel.annotation.ExcelContent;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.util.CollectionUtils;

/**
 * @description
 * @date 2023/09/24
 **/
@Slf4j
public class ObjectCollectionConverter implements CommonConverter<Collection<Object>> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Collection.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToExcelData(Collection<Object> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return StringUtils.EMPTY;
        }
        Object object = collection.stream().findFirst().get();
        String methodName = Optional.ofNullable(object.getClass().getAnnotation(ExcelContent.class))
            .map(ExcelContent::method)
            .orElse("toString");
        return collection.stream().map(e -> {
            try {
                return (String) MethodUtils.invokeMethod(e, methodName);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                log.error("Failed to invokeMethod finalMethodName", ex);
            }
            return StringUtils.EMPTY;
        }).collect(Collectors.joining("; "));
    }
}
