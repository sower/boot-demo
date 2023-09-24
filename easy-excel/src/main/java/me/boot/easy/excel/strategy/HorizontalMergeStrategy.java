package me.boot.easy.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @description
 * @date 2023/09/15
 **/
public class HorizontalMergeStrategy implements CellWriteHandler {

    /**
     * 需要合并行的下标，从0开始
     */
    private final int[] mergeRowIndex;

    /**
     * 从第几列开始合并，表头下标为0
     */
    private final int startMergedColumn;

    public HorizontalMergeStrategy(int startMergedColumn, int[] mergeRowIndex) {
        this.startMergedColumn = startMergedColumn;
        this.mergeRowIndex = mergeRowIndex;
    }


    public void afterCellDispose(WriteSheetHolder writeSheetHolder,
        WriteTableHolder writeTableHolder, List<WriteCellData<?>> list, Cell cell, Head head,
        Integer integer, Boolean aBoolean) {

        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();

        if (curColIndex > startMergedColumn) {
            for (int rowIndex : mergeRowIndex) {
                if (curRowIndex == rowIndex) {
                    mergeWithPrevRow(writeSheetHolder, cell, curRowIndex, curColIndex);
                    break;
                }
            }
        }

    }

    private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex,
        int curColIndex) {

        //获取当前行的当前列的数据和上一行的当前列列数据，通过上一行数据是否相同进行合并
        Object curData = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() :
            cell.getNumericCellValue();

        Row row = cell.getSheet().getRow(curRowIndex );
        if (row == null) {
            // 当获取不到行数据时，使用缓存sheet中数据
            row = writeSheetHolder.getCachedSheet().getRow(curRowIndex );
        }

        Cell preCell = row.getCell(curColIndex-1);
        Object preData =
            preCell.getCellType() == CellType.STRING ? preCell.getStringCellValue() :
                preCell.getNumericCellValue();
        // 比较当前行的第一列的单元格与上一行是否相同，相同合并当前单元格与上一行
        if (curData.equals(preData)) {
            Sheet sheet = writeSheetHolder.getSheet();
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            boolean isMerged = false;
            for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                if (cellRangeAddr.isInRange(curRowIndex, curColIndex - 1)) {
                    sheet.removeMergedRegion(i);
                    cellRangeAddr.setLastColumn(curColIndex);
                    sheet.addMergedRegion(cellRangeAddr);
                    isMerged = true;
                }
            }
            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex ,
                    curRowIndex, curColIndex- 1,
                    curColIndex);
                sheet.addMergedRegion(cellRangeAddress);
            }
        }
    }
}


