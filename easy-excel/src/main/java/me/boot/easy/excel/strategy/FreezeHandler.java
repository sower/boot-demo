package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @description
 * @date 2023/09/26
 **/
public class FreezeHandler implements SheetWriteHandler {

    public int colSplit = 0;
    public int rowSplit = 1;

    public FreezeHandler(int colSplit, int rowSplit) {
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.createFreezePane(colSplit, rowSplit);
    }
}
