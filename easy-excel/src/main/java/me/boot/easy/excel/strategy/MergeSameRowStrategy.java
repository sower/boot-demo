package me.boot.easy.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.Collection;
import java.util.List;
import lombok.NoArgsConstructor;
import me.boot.easy.excel.util.Cells;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.lang.Nullable;

/**
 * MergeSameRowStrategy
 *
 * @since 2023/09/15
 */
@NoArgsConstructor
public class MergeSameRowStrategy implements CellWriteHandler {

    // 开始合并行
    private int startRow;

    private int endRow = Integer.MAX_VALUE;

    // 需要合并列的下标，从0开始
    private Collection<Integer> mergeColumns;

    public MergeSameRowStrategy(
        int startRow, int endRow, @Nullable Collection<Integer> mergeColumns) {
        this.startRow = Math.max(startRow, 0);
        this.endRow = endRow <= 0 ? Integer.MAX_VALUE : endRow;
        this.mergeColumns =
            CollectionUtils.filterInverse(mergeColumns, item -> item < 0) ? null : mergeColumns;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder,
        WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell,
        Head head, Integer relativeRowIndex, Boolean isHead) {
        // 已合并的表头跳过
        if (writeSheetHolder.getAutomaticMergeHead() && isHead) {
            return;
        }

        int curRowIndex = cell.getRowIndex();
        if (curRowIndex <= startRow || curRowIndex > endRow) {
            return;
        }

        int curColIndex = cell.getColumnIndex();
        if (mergeColumns == null || mergeColumns.contains(curColIndex)) {
            mergeWithPreRow(writeSheetHolder.getCachedSheet(), cell, curRowIndex, curColIndex);
        }
    }

    // 获取当前行的当前列的数据和上一行的当前列列数据，通过上一行数据是否相同进行合并
    private void mergeWithPreRow(Sheet sheet, Cell cell, int curRowIndex, int curColIndex) {
        Row preRow = sheet.getRow(curRowIndex - 1);
        Cell preCell = preRow.getCell(curColIndex);

        if (!Cells.equals(cell, preCell)) {
            return;
        }
        boolean isMerged = false;
        List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
        for (int index = 0; index < mergeRegions.size(); index++) {
            CellRangeAddress mergeRegion = sheet.getMergedRegion(index);
            // 之前的单元格已被合并，先移除，再重新添加
            if (mergeRegion.isInRange(preCell)) {
                sheet.removeMergedRegion(index);
                mergeRegion.setLastRow(curRowIndex);
                sheet.addMergedRegionUnsafe(mergeRegion);
                isMerged = true;
                break;
            }
        }

        // 若上一个单元格未被合并，则新增合并单元
        if (!isMerged) {
            sheet.addMergedRegion(
                new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex));
        }
    }
}
