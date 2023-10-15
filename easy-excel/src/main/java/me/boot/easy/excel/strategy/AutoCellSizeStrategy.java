package me.boot.easy.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @description
 * @date 2023/09/10
 **/
public class AutoCellSizeStrategy extends AbstractColumnWidthStyleStrategy {

    private int maxColumnWidth = 50;
    private int maxRowHeight = 5;

    public AutoCellSizeStrategy() {
    }

    public AutoCellSizeStrategy(int maxColumnWidth, int maxRowHeight) {
        this.maxColumnWidth = Math.max(maxColumnWidth, 10);;
        this.maxRowHeight = maxRowHeight;
    }

    private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(
        8);

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder,
        List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex,
        Boolean isHead) {
        boolean needSetWidth = !isHead && CollectionUtils.isNotEmpty(cellDataList);
        if (!needSetWidth) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = this.cache.computeIfAbsent(
            writeSheetHolder.getSheetNo(), (key) -> new HashMap<>(16));
        Integer dataLength = this.dataLength(cellDataList, cell, isHead);

        if (dataLength <= 0) {
            return;
        }
        dataLength = Math.min(dataLength, maxColumnWidth);
        Integer columnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
        if (columnWidth == null || dataLength > columnWidth) {
            maxColumnWidthMap.put(cell.getColumnIndex(), dataLength);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), dataLength * 256);
            if (maxRowHeight > 0) {
                double ratio = Math.ceil(1d * dataLength / maxColumnWidth);
                double rowHeight = 14 * 20 * Math.min(ratio, maxRowHeight);
                cell.getRow().setHeight((short) rowHeight);
            }
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes(StandardCharsets.UTF_8).length;
        }
        WriteCellData<?> cellData = cellDataList.get(0);

        switch (cellData.getType()) {
            case STRING:
                return cellData.getStringValue().getBytes(StandardCharsets.UTF_8).length + 2;
            case BOOLEAN:
                return cellData.getBooleanValue().toString()
                    .getBytes(StandardCharsets.UTF_8).length;
            case NUMBER:
                return cellData.getNumberValue().toString()
                    .getBytes(StandardCharsets.UTF_8).length;
            case DATE:
                return
                    cellData.getDateValue().toString().getBytes(StandardCharsets.UTF_8).length + 2;
            default:
                return -1;
        }

    }

}
