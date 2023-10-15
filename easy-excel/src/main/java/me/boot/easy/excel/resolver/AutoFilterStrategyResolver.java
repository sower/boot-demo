package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import me.boot.easy.excel.annotation.AutoFilter;
import me.boot.easy.excel.strategy.AutoFilterHandler;

/**
 * @description
 * @date 2023/10/10
 **/
public class AutoFilterStrategyResolver implements WriteStrategyResolver {

    @Override
    public void resolveHeadClass(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        Class<?> headClass) {
        AutoFilter annotation = headClass.getAnnotation(AutoFilter.class);
        if (annotation != null) {
            excelWriterSheetBuilder.registerWriteHandler(
                new AutoFilterHandler(annotation.firstRow(), annotation.lastRow(),
                    annotation.firstCol(), annotation.lastCol()));
        }
    }
}
