package me.boot.easy.excel.util;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.boot.easy.excel.converter.CommonConverter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

/**
 * @description
 * @date 2023/09/24
 **/
public class TransposeUtil {

    public static List<List<Object>> getHeaders(List<?> data) {
        List<Field> fields = getExcelPropertyFields(data);
        return fields.stream()
            .map(field -> {
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                Converter<?> converter = null;
                if (CommonConverter.class.isAssignableFrom(annotation.converter())) {
                    converter = BeanUtils.instantiateClass(annotation.converter());
                }
                return Lists.newArrayList(String.join(" ", annotation.value()), converter);
            })
            .collect(Collectors.toList());

    }


    public static List<List<Object>> getContents(List<?> data) {
        List<BeanMap> beanMaps = data.stream().map(BeanMap::create).collect(Collectors.toList());
        List<Field> fields = getExcelPropertyFields(data);
        return fields.stream().map(Field::getName)
            .map(e -> beanMaps.stream().map(beanMap -> beanMap.get(e)).collect(Collectors.toList()))
            .collect(Collectors.toList());

    }

    private static List<Field> getExcelPropertyFields(List<?> data) {
        return FieldUtils.getFieldsListWithAnnotation(
            data.get(0).getClass(), ExcelProperty.class);
    }

    public static List<List<String>> transpose(List<?> data) {
        List<List<Object>> headers = getHeaders(data);
        List<List<Object>> contents = getContents(data);
        List<List<String>> table = new ArrayList<>(headers.size());
        for (int i = 0; i < contents.size(); i++) {
            List<Object> header = headers.get(i);
            List<Object> content = contents.get(i);
            List<String> row = new ArrayList<>(header.size());
            row.add((String) header.get(0));
            CommonConverter<Object> converter = (CommonConverter<Object>) header.get(1);
            if (converter != null) {
                content.stream().map(converter::convertToExcelData)
                    .forEach(row::add);
            } else {
                content.forEach(e -> row.add((String) e));
            }
            table.add(row);
        }
        return table;
    }

}
