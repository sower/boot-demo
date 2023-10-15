package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import me.boot.easy.excel.annotation.MergeSameColumn;
import me.boot.easy.excel.strategy.MergeSameColumnStrategy;

/**
 * @description
 * @date 2023/10/10
 **/
public class MergeSameColumnStrategyResolver implements WriteStrategyResolver {

    @Override
    public void resolveHeadClass(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        Class<?> headClass) {
        MergeSameColumn annotation = headClass.getAnnotation(MergeSameColumn.class);
        if (annotation == null) {
            return;
        }
        Set<Integer> mergedRows = null;
        if (annotation.mergeRows() != null) {
            mergedRows = Arrays.stream(annotation.mergeRows()).boxed().collect(Collectors.toSet());
        }
        excelWriterSheetBuilder.registerWriteHandler(
            new MergeSameColumnStrategy(annotation.startColumn(), annotation.endColumn(),
                mergedRows));
    }
}
