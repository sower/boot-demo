package me.boot.easy.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @description
 * @date 2023/09/15
 **/
public class HorizontalHeaderStyleStrategy implements CellWriteHandler {

    private final int lastHeaderIndex;

    public HorizontalHeaderStyleStrategy() {
        this(0);
    }

    public HorizontalHeaderStyleStrategy(int lastHeaderIndex) {
        this.lastHeaderIndex = lastHeaderIndex;
    }


    /**
     * 在单元上的所有操作完成后调用
     */
    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder,
        WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell,
        Head head, Integer relativeRowIndex, Boolean isHead) {

        Workbook workbook = cell.getSheet().getWorkbook();

        HorizontalCellStyleStrategy horizontalCellStyle = ExcelStyle.defaultCenterStyle();
        WriteCellStyle writeCellStyle =
            cell.getColumnIndex() == lastHeaderIndex ?
                horizontalCellStyle.getHeadWriteCellStyle()
                : horizontalCellStyle.getContentWriteCellStyleList().get(0);

        CellStyle cellStyle =
            StyleUtil.buildCellStyle(workbook, null, writeCellStyle);

        cellStyle.setFont(StyleUtil.buildFont(workbook, null, writeCellStyle.getWriteFont()));
        cell.setCellStyle(cellStyle);

        //设置行高
//                    writeSheetHolder.getSheet().getRow(cell.getRowIndex()).setHeight((short) (1.4 * 256));
//                    // 单元格策略
//                    WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//                    // 设置背景颜色白色
//                    contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//                    // 设置垂直居中为居中对齐
//                    contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//                    // 设置左右对齐为靠左对齐
//                    contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
//                    // 设置单元格上下左右边框为细边框
//                    contentWriteCellStyle.setBorderBottom(BorderStyle.MEDIUM);
//                    contentWriteCellStyle.setBorderLeft(BorderStyle.MEDIUM);
//                    contentWriteCellStyle.setBorderRight(BorderStyle.MEDIUM);
//                    contentWriteCellStyle.setBorderTop(BorderStyle.MEDIUM);
//                    // 创建字体实例
//                    WriteFont cellWriteFont = new WriteFont();
//                    // 设置字体大小
//                    cellWriteFont.setFontName("宋体");
//                    cellWriteFont.setFontHeightInPoints((short) 14);

//                    //单元格颜色
//                    contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//                    contentWriteCellStyle.setWriteFont(cellWriteFont);

    }
}







