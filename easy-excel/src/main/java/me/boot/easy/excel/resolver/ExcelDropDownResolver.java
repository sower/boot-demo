package me.boot.easy.excel.resolver;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import me.boot.easy.excel.annotation.ExcelDropDown;
import me.boot.easy.excel.property.ExcelDropDownProperty;
import me.boot.easy.excel.strategy.DropDownHandler;

/**
 * @description
 * @date 2023/09/29
 **/
public class ExcelDropDownResolver {

    public static Map<Integer, ExcelDropDownProperty> resolve(Class<?> headClass) {
        Map<Integer, ExcelDropDownProperty> map = new HashMap<>(4);

        // todo 忽略注解需要前置处理
        boolean ignore = headClass.getAnnotation(ExcelIgnoreUnannotated.class) != null;
        Field[] fields = headClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getAnnotation(ExcelIgnore.class) != null) {
                continue;
            }

            ExcelDropDownProperty dropDownProperty = ExcelDropDownProperty.of(
                field.getAnnotation(ExcelDropDown.class));
            if (dropDownProperty == null) {
                continue;
            }

            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty == null && ignore) {
                continue;
            }

            if (dropDownProperty.getStartRow() < 0) {
//                Optional.ofNullable(excelProperty).map()
                dropDownProperty.setStartRow(1);
            }
            int columnIndex = i;
            if (excelProperty != null) {
                columnIndex = excelProperty.index() < 0 ? columnIndex : excelProperty.index();
            }
            map.put(columnIndex, dropDownProperty);
        }
        return map;
    }

    public static DropDownHandler getDropDownHandler(Class<?> headClass) {
        Map<Integer, ExcelDropDownProperty> map = resolve(headClass);
        if (map.isEmpty()) {
            return null;
        }
        return new DropDownHandler(map);
    }
}
