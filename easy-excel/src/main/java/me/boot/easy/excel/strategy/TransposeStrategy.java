package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.util.List;
import me.boot.easy.excel.model.SheetObject;
import me.boot.easy.excel.spi.SheetWriterHandler;
import me.boot.easy.excel.util.TransposeUtils;

/**
 * TransposeStrategy
 *
 * @since 2023/10/14
 */
public class TransposeStrategy implements SheetWriterHandler {

    @Override
    public void beforeHandleSheetWriter(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        SheetObject sheetObject) {
        if (sheetObject.getHeadClass() != null) {
            sheetObject.setHeadClass(null);
            List<?> data = (List<?>) sheetObject.getData();
            List<List<String>> transposeList = TransposeUtils.transpose(data);
            sheetObject.setData(transposeList);
        }
    }

    @Override
    public void afterHandleSheetWriter(ExcelWriterSheetBuilder excelWriterSheetBuilder,
        SheetObject sheetObject) {
        excelWriterSheetBuilder.head(Void.class);
    }
}
