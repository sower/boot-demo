package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

/**
 * @description
 * @date 2023/09/10
 **/
@Data
@AllArgsConstructor
public class MergeSameRowStrategy implements RowWriteHandler {

    //合并的起始行：key：开始，value；结束
    private Map<Integer, Integer> map;

    //要合并的列
    private int[] cols;

    public void afterRowDispose(
        WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
        Integer relativeRowIndex, Boolean isHead) {
        int lastRowNum = writeSheetHolder.getSheet().getLastRowNum();
        if (lastRowNum != relativeRowIndex) {
            return;
        }
//        TODO 待实现
//        writeSheetHolder.getSheet().

    }

//    @Override
//    public void afterRowDispose(RowWriteHandlerContext context) {
//        //如果是head或者当前行不是合并的起始行，跳过
//        if (context.getHead() || !map.containsKey(context.getRowIndex())) {
//            return;
//        }
//        Integer endRow = map.get(context.getRowIndex());
//        if (!context.getRowIndex().equals(endRow)) {
//            //编列合并的列，合并行
//            for (int col : cols) {
//                // CellRangeAddress(起始行,结束行,起始列,结束列)
//                context.getWriteSheetHolder().getSheet().addMergedRegionUnsafe(
//                    new CellRangeAddress(context.getRowIndex(), endRow, col, col));
//            }
//        }
//    }
}
