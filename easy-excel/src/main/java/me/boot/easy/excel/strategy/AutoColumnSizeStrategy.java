package me.boot.easy.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 自动列宽策略
 * <p>适用于内容存在换行场景</p>
 *
 * @since 2023/09/10
 **/
@NoArgsConstructor
public class AutoColumnSizeStrategy extends AbstractColumnWidthStyleStrategy {

    private int maxColumnWidth = 50;

    private List<Integer> lineLengths;

    protected int getMaxColumnWidth() {
        return maxColumnWidth;
    }

    protected List<Integer> getLineLengths() {
        return lineLengths;
    }

    public AutoColumnSizeStrategy(int maxColumnWidth) {
        this.maxColumnWidth = Math.max(maxColumnWidth, 10);
    }

    private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(
        8);

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder,
        List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex,
        Boolean isHead) {
        boolean needSetWidth = isHead || CollectionUtils.isNotEmpty(cellDataList);
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
            setRowHeight(cell);
        }
    }

    protected void setRowHeight(Cell cell) {
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        lineLengths = null;
        if (isHead) {
            return cell.getStringCellValue().getBytes(StandardCharsets.UTF_8).length;
        }
        WriteCellData<?> cellData = IterableUtils.first(cellDataList);

        switch (cellData.getType()) {
            case STRING:
                lineLengths = Arrays.stream(
                        cellData.getStringValue().split(StringUtils.LF))
                    .map(str -> str.getBytes(StandardCharsets.UTF_8).length)
                    .collect(Collectors.toList());
                return lineLengths.isEmpty() ? -1 : Collections.max(lineLengths);
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
