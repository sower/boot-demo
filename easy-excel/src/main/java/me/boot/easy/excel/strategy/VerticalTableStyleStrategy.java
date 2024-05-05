package me.boot.easy.excel.strategy;

import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * VerticalTableStyleStrategy
 *
 * @since 2023/09/15
 */
public class VerticalTableStyleStrategy implements RowWriteHandler {

    private final int lastHeaderIndex;

    @Setter
    private HorizontalCellStyleStrategy cellStyleStrategy;

    public VerticalTableStyleStrategy() {
        this(0);
    }

    public VerticalTableStyleStrategy(int lastHeaderIndex) {
        this.lastHeaderIndex = lastHeaderIndex;
        cellStyleStrategy = ExcelStyle.defaultCenterStyle();
    }


    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
        CellStyle headerStyle = getCellStyle(workbook, cellStyleStrategy.getHeadWriteCellStyle());
        CellStyle contentStyle = getCellStyle(workbook,
            cellStyleStrategy.getContentWriteCellStyleList().get(0));

        Row row = context.getRow();
        int index = lastHeaderIndex;
        for (Cell cell : row) {
            if (index-- >= 0) {
                cell.setCellStyle(headerStyle);
            } else {
                cell.setCellStyle(contentStyle);
            }
        }
    }

    private CellStyle getCellStyle(Workbook workbook, WriteCellStyle writeCellStyle) {
        CellStyle cellStyle =
            StyleUtil.buildCellStyle(workbook, null, writeCellStyle);
        cellStyle.setFont(StyleUtil.buildFont(workbook, null, writeCellStyle.getWriteFont()));
        return cellStyle;
    }
}







