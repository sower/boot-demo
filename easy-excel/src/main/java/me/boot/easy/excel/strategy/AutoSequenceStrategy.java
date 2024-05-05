package me.boot.easy.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * AutoSequenceStrategy
 *
 * @since 2023/09/15
 */
public class AutoSequenceStrategy implements RowWriteHandler, SheetWriteHandler {


    private int startColumn;


    public AutoSequenceStrategy() {
    }

    public AutoSequenceStrategy(int startColumn) {
        this.startColumn = Math.max(startColumn, 0);
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        // TODO 多行头支持
        // 修改存储头部 map
        ExcelWriteHeadProperty excelWriteHeadProperty = writeSheetHolder.excelWriteHeadProperty();
        Map<Integer, Head> headMap = excelWriteHeadProperty.getHeadMap();
        int size = headMap.size();
        for (int current = size; current > 0; current--) {
            int previous = current - 1;
            headMap.put(current, headMap.get(previous));
        }
        // 空出第一列
        headMap.remove(0);
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
        Row row, Integer relativeRowIndex, Boolean isHead) {
        Cell cell = row.createCell(0);
        if (isHead) {
            cell.setCellValue("序号");
            return;
        }
        cell.setCellValue(++startColumn);
    }


    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder,
        WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (row.getLastCellNum() > 1) {
            // 将自定义新增的序号列的样式设置与默认的样式一致
            row.getCell(0).setCellStyle(row.getCell(1).getCellStyle());
        }

    }
}
