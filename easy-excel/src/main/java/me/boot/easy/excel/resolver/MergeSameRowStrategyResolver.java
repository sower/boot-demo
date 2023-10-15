package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import me.boot.easy.excel.annotation.MergeSameRow;
import me.boot.easy.excel.strategy.MergeSameRowStrategy;

/**
 * @description
 * @date 2023/10/10
 **/
public class MergeSameRowStrategyResolver implements WriteStrategyResolver {

    @Override
    public void resolveHeadClass(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        Class<?> headClass) {
        MergeSameRow annotation = headClass.getAnnotation(MergeSameRow.class);
        if (annotation == null) {
            return;
        }
        Set<Integer> mergedColumns = null;
        if (annotation.mergeColumns() != null) {
            mergedColumns = Arrays.stream(annotation.mergeColumns()).boxed()
                .collect(Collectors.toSet());
        }
        excelWriterSheetBuilder.registerWriteHandler(
            new MergeSameRowStrategy(annotation.startRow(), annotation.endRow(), mergedColumns));
    }
}
