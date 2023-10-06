package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @description
 * @date 2023/09/26
 **/
public class AutoFilterHandler implements SheetWriteHandler {

    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;

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
        // Todo: handle no head
        firstRow = Math.max(firstRow, 0);
        firstCol = Math.max(firstCol, 0);
        if (lastRow < 0) {
            lastRow = writeSheetHolder.getExcelWriteHeadProperty().getHeadRowNumber();
        }
        if (lastCol < 0) {
            lastCol = writeSheetHolder.getExcelWriteHeadProperty().getHeadMap().keySet().size();
        }
        sheet.setAutoFilter(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

}
