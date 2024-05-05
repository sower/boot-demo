package me.boot.easy.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.io.File;
import java.io.OutputStream;
import java.util.List;
import me.boot.easy.excel.model.ExcelObject;
import me.boot.easy.excel.model.SheetObject;
import me.boot.easy.excel.resolver.WriteStrategyResolver;
import me.boot.easy.excel.spi.ExcelWriterHandler;
import me.boot.easy.excel.spi.ServiceLoaderUtils;
import me.boot.easy.excel.spi.SheetWriterHandler;
import org.apache.commons.collections4.CollectionUtils;

/**
 * ExcelWriteResolver
 *
 * @since 2023/10/06
 */
public class ExcelWriteResolver {

    private static final List<ExcelWriterHandler> EXCEL_WRITER_HANDLERS = ServiceLoaderUtils.excelWriterHandlers();
    private static final List<SheetWriterHandler> SHEET_WRITER_HANDLERS = ServiceLoaderUtils.sheetWriterHandlers();

    public static void resolve(ExcelObject excelObject) {
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(
            excelObject.getName() + excelObject.getType().getValue());
        writeExcel(excelObject, excelWriterBuilder);
    }

    public static void resolve(ExcelObject excelObject, OutputStream outputStream) {
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(outputStream);
        writeExcel(excelObject, excelWriterBuilder);
    }

    public static void resolve(ExcelObject excelObject, File file) {
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(file);
        writeExcel(excelObject, excelWriterBuilder);
    }

    public static void writeExcel(ExcelObject excelObject,
        ExcelWriterBuilder excelWriterBuilder) {
        excelWriterBuilder
            .excelType(excelObject.getType());

        // before
        EXCEL_WRITER_HANDLERS.forEach(
            excelWriterHandler -> excelWriterHandler.handleExcelWriter(excelWriterBuilder,
                excelObject)
        );

        if (CollectionUtils.isNotEmpty(excelObject.getWriteHandlers())) {
            excelObject.getWriteHandlers()
                .forEach(excelWriterBuilder::registerWriteHandler);
        }

        try (ExcelWriter excelWriter = excelWriterBuilder.build()) {
            excelObject.getSheets().forEach(sheet -> writeSheet(sheet, excelWriter));
        }
    }

    private static void writeSheet(SheetObject sheetObject, ExcelWriter excelWriter) {
        ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(sheetObject.getSheetName());
        SHEET_WRITER_HANDLERS.forEach(
            sheetWriterHandler -> sheetWriterHandler.beforeHandleSheetWriter(sheetBuilder,
                sheetObject));
        if (sheetObject.getHeadClass() != null) {
            sheetBuilder.head(sheetObject.getHeadClass());
            List<WriteStrategyResolver> strategyResolvers = ServiceLoaderUtils.writeStrategyResolvers();
            strategyResolvers.forEach(
                writeStrategyResolver -> writeStrategyResolver.resolveExcelObject(sheetBuilder,
                    sheetObject));
        }
        if (sheetObject.getHead() != null) {
            sheetBuilder.head(sheetObject.getHead());
        }

        // after
        SHEET_WRITER_HANDLERS.forEach(
            sheetWriterHandler -> sheetWriterHandler.afterHandleSheetWriter(sheetBuilder,
                sheetObject));

        excelWriter.write(sheetObject.getData(), sheetBuilder.build());
    }

}
