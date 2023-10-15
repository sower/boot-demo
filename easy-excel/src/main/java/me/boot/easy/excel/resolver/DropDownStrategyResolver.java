package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.lang.reflect.Field;
import me.boot.easy.excel.annotation.ExcelDropDown;
import me.boot.easy.excel.strategy.DropDownHandler;

/**
 * @description
 * @date 2023/10/10
 **/
public class DropDownStrategyResolver implements WriteStrategyResolver {

    @Override
    public void resolveHeadClass(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        Class<?> headClass) {
        for (Field field : headClass.getDeclaredFields()) {
            if (field.getAnnotation(ExcelDropDown.class) != null) {
                excelWriterSheetBuilder.registerWriteHandler(new DropDownHandler());
                return;
            }
        }

    }
}
