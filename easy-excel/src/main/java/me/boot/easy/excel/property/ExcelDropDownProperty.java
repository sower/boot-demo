package me.boot.easy.excel.property;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boot.easy.excel.annotation.ExcelDropDown;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @description
 * @date 2023/09/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDropDownProperty {

    /**
     * 固定下拉内容
     */
    String[] constraints;


    /**
     * 下拉框的起始行，默认head下一行
     */
    int startRow = -1;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    int endRow = 1000;

    public static ExcelDropDownProperty of(ExcelDropDown excelDropDown) {
        if (excelDropDown == null) {
            return null;
        }

        Class<? extends Enum<?>>[] enumClasses = excelDropDown.enumClasses();
        String[] enumValues = Arrays.stream(enumClasses).map(ExcelDropDownProperty::getEnumValues)
            .flatMap(Collection::stream).toArray(String[]::new);

        String[] constraints = ArrayUtils.addAll(excelDropDown.values(), enumValues);
        return new ExcelDropDownProperty(constraints,
            excelDropDown.startRow(), excelDropDown.endRow());
    }

    private static Set<String> getEnumValues(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(Enum::name)
            .collect(Collectors.toSet());
    }
}
