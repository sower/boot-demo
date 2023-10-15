package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import me.boot.easy.excel.annotation.AutoCellSize;
import me.boot.easy.excel.strategy.AutoCellSizeStrategy;

/**
 * @description
 * @date 2023/10/10
 **/
public class AutoCellSizeStrategyResolver implements WriteStrategyResolver {

    @Override
    public void resolveHeadClass(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        Class<?> headClass) {
        AutoCellSize annotation = headClass.getAnnotation(AutoCellSize.class);
        if (annotation != null) {
            excelWriterSheetBuilder.registerWriteHandler(
                new AutoCellSizeStrategy(annotation.maxColumnWidth(), annotation.maxRowHeight()));
        }
    }
}
