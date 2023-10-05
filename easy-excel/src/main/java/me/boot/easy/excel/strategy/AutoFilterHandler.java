package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.handler.context.WorkbookWriteHandlerContext;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @description
 * @date 2023/09/26
 **/
public class AutoFilterHandler implements WorkbookWriteHandler {

    public String autoFilterRange = "1:1";

//    @Override
//    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
//        Sheet sheet = writeSheetHolder.getSheet();
//        sheet.setAutoFilter(CellRangeAddress.valueOf(autoFilterRange));
//    }

    @Override
    public void afterWorkbookDispose(WorkbookWriteHandlerContext context) {
        Sheet sheet = context.getWriteContext().writeSheetHolder().getSheet();
        sheet.setAutoFilter(CellRangeAddress.valueOf(autoFilterRange));
    }
}
