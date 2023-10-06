package me.boot.easy.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.boot.easy.excel.annotation.ExcelFreeze;
import me.boot.easy.excel.controller.ExcelResponse;
import me.boot.easy.excel.strategy.FreezeHandler;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @description
 * @date 2023/10/06
 **/
public class ExcelWriteResolver {

    public static void write(ExcelResponse excelResponse) {

        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(
            excelResponse.getName() + excelResponse.getType().getValue());
        buildExcel(excelResponse, excelWriterBuilder);
    }

    public static void write(ExcelResponse excelResponse, OutputStream outputStream) {
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(outputStream);
        buildExcel(excelResponse, excelWriterBuilder);
    }


    public static void buildExcel(ExcelResponse excelResponse,
        ExcelWriterBuilder excelWriterBuilder) {
        excelWriterBuilder
            .excelType(excelResponse.getType());

        if (CollectionUtils.isNotEmpty(excelResponse.getWriteHandlers())) {
            excelResponse.getWriteHandlers()
                .forEach(excelWriterBuilder::registerWriteHandler);
        }

        ExcelWriterSheetBuilder sheet = excelWriterBuilder
            .sheet(excelResponse.getSheetName());
        if (excelResponse.getHeadClass() != null) {
            sheet.head(excelResponse.getHeadClass());
            getWriteHandlers(excelResponse.getHeadClass())
                .forEach(sheet::registerWriteHandler);
        }
        if (excelResponse.getHead() != null) {
            sheet.head(excelResponse.getHead());
        }
        sheet.doWrite(excelResponse.getData());

    }


    public static List<WriteHandler> getWriteHandlers(Class<?> headClass) {
        List<WriteHandler> writeHandlers = new LinkedList<>();
        writeHandlers.add(getFreezeHandler(headClass));

        return writeHandlers.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static WriteHandler getFreezeHandler(Class<?> headClass) {
        ExcelFreeze annotation = headClass.getAnnotation(ExcelFreeze.class);
        if (annotation == null) {
            return null;
        }
        return new FreezeHandler(annotation.rowIndex(), annotation.rowIndex());
    }


}
