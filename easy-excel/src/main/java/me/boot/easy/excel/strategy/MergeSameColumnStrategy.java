package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.boot.easy.excel.util.CellUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @description
 * @date 2023/09/15
 **/
public class MergeSameColumnStrategy implements RowWriteHandler {

    /**
     * 需要合并行的下标，从0开始
     */
    private final Collection<Integer> mergeRowIndex;

    /**
     * 从第几列开始合并，表头下标为0
     */
    private final int startMergedColumn;

    public MergeSameColumnStrategy(int startMergedColumn, Collection<Integer> mergeRowIndex) {
        this.startMergedColumn = startMergedColumn;
        this.mergeRowIndex = mergeRowIndex;
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder,
        WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (!mergeRowIndex.contains(relativeRowIndex)) {
            return;
        }

        Map<Integer, Integer> mergeMap = getMergeMap(row);
        mergeMap.forEach((startCol, endCol) -> row.getSheet().addMergedRegionUnsafe(
            new CellRangeAddress(relativeRowIndex, relativeRowIndex, startCol, endCol)));

    }

    // 获取当前行相同值的 开始列：结束列 map
    private Map<Integer, Integer> getMergeMap(Row row) {
        Map<Integer, Integer> mergeMap = new HashMap<>(4);
        int cols = row.getPhysicalNumberOfCells();
        for (int pos = startMergedColumn; pos < cols; ) {
            Object data = CellUtil.getCellValue(row.getCell(pos));
            int start = pos++;
            while (pos < cols && Objects.equals(data, CellUtil.getCellValue(row.getCell(pos)))) {
                ++pos;
            }
            if (start != pos - 1) {
                mergeMap.put(start, pos - 1);
            }
        }
        return mergeMap;
    }


}


