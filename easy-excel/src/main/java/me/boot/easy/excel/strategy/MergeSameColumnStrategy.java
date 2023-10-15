package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.boot.easy.excel.util.Cells;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.lang.Nullable;

/**
 * @description
 * @date 2023/09/15
 */
public class MergeSameColumnStrategy implements RowWriteHandler {

    /**
     * 从第几列开始合并，表头下标为0
     */
    private int startColumn;

    /**
     * 合并的结束列
     */
    private int endColumn;

    /**
     * 需要合并行的下标，从0开始
     */
    private Collection<Integer> mergeRows;

    public MergeSameColumnStrategy() {
    }

    public MergeSameColumnStrategy(int startColumn, int endColumn,
        @Nullable Collection<Integer> mergeRows) {
        this.startColumn = Math.max(startColumn, 0);
        this.endColumn = endColumn;
        this.mergeRows = CollectionUtils.filterInverse(mergeRows, item -> item < 0) ? null : mergeRows;
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder,
        WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (mergeRows != null && !mergeRows.contains(relativeRowIndex)) {
            return;
        }

        if (endColumn <= 0) {
            endColumn = Math.max(endColumn, row.getPhysicalNumberOfCells());
        }

        Map<Integer, Integer> mergeMap = getMergeMap(row);
        mergeMap.forEach((startCol, endCol) -> row.getSheet().addMergedRegionUnsafe(
            new CellRangeAddress(relativeRowIndex, relativeRowIndex, startCol, endCol)));

    }

    // 获取当前行相同值的 开始列：结束列 map
    private Map<Integer, Integer> getMergeMap(Row row) {
        Map<Integer, Integer> mergeMap = new HashMap<>(4);
        for (int pos = startColumn; pos < endColumn; ) {
            Object data = Cells.getValue(row.getCell(pos));
            int start = pos++;
            while (pos < endColumn && Objects.equals(data, Cells.getValue(row.getCell(pos)))) {
                ++pos;
            }
            if (start != pos - 1) {
                mergeMap.put(start, pos - 1);
            }
        }
        return mergeMap;
    }
}
