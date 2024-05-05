package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * FreezeHandler
 *
 * @since 2023/09/26
 */
@NoArgsConstructor
public class FreezeHandler implements SheetWriteHandler {

    private int rowSplit = -1;
    private int colSplit = 0;

    public FreezeHandler(int rowSplit, int colSplit) {
        this.rowSplit = rowSplit;
        this.colSplit = Math.max(colSplit, 0);
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();

        if (rowSplit < 0) {
            rowSplit = writeSheetHolder.getExcelWriteHeadProperty().getHeadRowNumber();
        }
        sheet.createFreezePane(colSplit, rowSplit);
    }
}
