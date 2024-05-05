package me.boot.easy.excel.strategy;

import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 自动单元格大小适应
 * <p>适用于有单元格合并场景</p>
 *
 * @since 2023/09/10
 **/
@NoArgsConstructor
public class AutoCellSizeStrategy extends AutoColumnSizeStrategy {

    private int maxRowHeight = 5;

    public AutoCellSizeStrategy(int maxColumnWidth, int maxRowHeight) {
        super(maxColumnWidth);
        this.maxRowHeight = maxRowHeight;
    }

    @Override
    protected void setRowHeight(Cell cell) {
        if (maxRowHeight <= 0) {
            return;
        }
        double rowHeight = getLineLengths().stream().map(this::getRowHeight).reduce(
            Double::sum).orElse(8d);
        cell.getRow().setHeight((short) rowHeight);
    }

    private double getRowHeight(int dataLength) {
        double ratio = Math.ceil(1d * dataLength / getMaxColumnWidth());
        return 14 * 20 * Math.min(ratio, maxRowHeight);
    }

}
