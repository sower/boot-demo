package me.boot.easy.excel.resolver;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import me.boot.easy.excel.model.SheetObject;

public interface WriteStrategyResolver {

    default void resolveExcelObject(ExcelWriterSheetBuilder writerSheetBuilder,
        SheetObject sheetObject) {
        resolveHeadClass(writerSheetBuilder, sheetObject.getHeadClass());
    }


    default void resolveHeadClass(ExcelWriterSheetBuilder writerSheetBuilder, Class<?> headClass) {
        throw new UnsupportedOperationException("Not support");
    }

}
