package me.boot.easy.excel.spi;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import me.boot.easy.excel.controller.ExcelObject;

/**
 * @description
 * @date 2023/10/13
 **/
public interface ExcelWriterHandler {

    void handleExcelWriter(ExcelWriterBuilder excelWriterBuilder, ExcelObject excelObject);

}
