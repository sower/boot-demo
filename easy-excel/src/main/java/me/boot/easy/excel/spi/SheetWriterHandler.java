package me.boot.easy.excel.spi;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import me.boot.easy.excel.model.SheetObject;

/**
 * SheetWriterHandler
 *
 * @since 2023/10/13
 */
public interface SheetWriterHandler {

    void beforeHandleSheetWriter(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        SheetObject sheetObject);


    void afterHandleSheetWriter(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        SheetObject sheetObject);
}
