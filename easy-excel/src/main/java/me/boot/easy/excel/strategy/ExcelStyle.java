package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.DefaultStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import java.util.Collections;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * @description
 * @since 2023/09/24
 **/
public class ExcelStyle {

    public static HorizontalCellStyleStrategy defaultCenterStyle() {
        // 内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 垂直居中,水平居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        // 自动换行
        contentWriteCellStyle.setWrapped(true);
        // 内容字体
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 使用默认头样式
        DefaultStyle defaultStyle = new DefaultStyle();
        defaultStyle.setContentWriteCellStyleList(Collections.singletonList(contentWriteCellStyle));
        return defaultStyle;
    }
}
