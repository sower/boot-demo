package me.boot.easy.excel.property;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boot.easy.excel.annotation.ExcelDropDown;
import org.apache.commons.lang3.ArrayUtils;

/**
 * ExcelDropDownProperty
 *
 * @since 2023/09/29
 */
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
        String[] enumValues = Arrays.stream(excelDropDown.enumClass().getEnumConstants())
            .map(Enum::name)
            .toArray(String[]::new);
        String[] constraints = ArrayUtils.addAll(excelDropDown.values(), enumValues);

        return new ExcelDropDownProperty(constraints, excelDropDown.startRow(),
            excelDropDown.endRow());
    }

}
