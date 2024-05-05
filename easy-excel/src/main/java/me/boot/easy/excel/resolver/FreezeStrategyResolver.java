package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import me.boot.easy.excel.annotation.FreezePane;
import me.boot.easy.excel.strategy.FreezeHandler;

/**
 * FreezeStrategyResolver
 *
 * @since 2023/10/10
 */
public class FreezeStrategyResolver implements WriteStrategyResolver {

    @Override
    public void resolveHeadClass(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        Class<?> headClass) {
        FreezePane annotation = headClass.getAnnotation(FreezePane.class);
        if (annotation != null) {
            excelWriterSheetBuilder.registerWriteHandler(
                new FreezeHandler(annotation.rowIndex(), annotation.colIndex()));
        }
    }
}
