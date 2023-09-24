package me.boot.easy.excel.util;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import me.boot.easy.excel.strategy.CommonConverter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

/**
 * @description
 * @date 2023/09/24
 **/
public class TransposeUtil {


    public static List<List<Object>> getHeaders(List<?> data) {
        Object object = data.get(0);
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(
            object.getClass(), ExcelProperty.class);
        return fields.stream()
            .map(field -> {
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                Converter<?> converter = null;
                if (CommonConverter.class.isAssignableFrom(annotation.converter())) {
                    converter = BeanUtils.instantiateClass(annotation.converter());
                }
                return Lists.newArrayList(annotation.value()[0], converter);
            })
            .collect(Collectors.toList());

    }


    public static List<List<Object>> getContents(List<?> data) {
        Object object = data.get(0);

        List<BeanMap> beanMaps = data.stream().map(BeanMap::create).collect(Collectors.toList());
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(
            object.getClass(), ExcelProperty.class);
        return fields.stream().map(Field::getName)
            .map(e -> beanMaps.stream().map(beanMap -> beanMap.get(e)).collect(Collectors.toList()))
            .collect(Collectors.toList());

    }

    public static List<List<Object>> transpose(List<?> data) {

        List<List<Object>> headers = getHeaders(data);
        List<List<Object>> contents = getContents(data);
        for (int i = 0; i < contents.size(); i++) {
            List<Object> header = headers.get(i);
            List<Object> content = contents.get(i);
            CommonConverter<Object> converter = (CommonConverter<Object>) header.get(1);
            if (converter != null) {
                content = content.stream().map(converter::convertToExcelData)
                    .collect(Collectors.toList());
                contents.set(i, content);
            }
            content.add(0, header.get(0));
        }
        return contents;
    }

}
