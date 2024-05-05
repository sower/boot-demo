package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * AutoFilterHandler
 *
 * @since 2023/09/26
 */
public class AutoFilterHandler implements SheetWriteHandler {

    private int firstRow;
    private int lastRow;
    private int firstCol = -1;
    private int lastCol = -1;

    public AutoFilterHandler() {
    }

    public AutoFilterHandler(int firstRow, int lastRow, int firstCol, int lastCol) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        firstRow = Math.max(firstRow, 0);
        firstCol = Math.max(firstCol, 0);
        if (lastRow < firstRow) {
            lastRow = writeSheetHolder.getExcelWriteHeadProperty().getHeadRowNumber();
        }
        if (lastCol < firstCol) {
            lastCol = writeSheetHolder.getExcelWriteHeadProperty().getHeadMap().keySet().size() - 1;
        }
        sheet.setAutoFilter(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

}
