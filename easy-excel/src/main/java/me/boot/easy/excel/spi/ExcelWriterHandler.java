package me.boot.easy.excel.spi;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import me.boot.easy.excel.model.ExcelObject;

/**
 * ExcelWriterHandler
 *
 * @since 2023/10/13
 */
public interface ExcelWriterHandler {

    void handleExcelWriter(ExcelWriterBuilder excelWriterBuilder, ExcelObject excelObject);

}
