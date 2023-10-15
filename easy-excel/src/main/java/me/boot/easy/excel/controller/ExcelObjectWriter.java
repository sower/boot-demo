package me.boot.easy.excel.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import me.boot.easy.excel.handler.ExcelWriteResolver;
import me.boot.easy.excel.strategy.ExcelStyle;

/**
 * @description
 * @date 2023/10/14
 **/
public class ExcelObjectWriter {

    private final ExcelObject excelObject = new ExcelObject();

    public static ExcelObjectWriter excel(String name) {
        ExcelObjectWriter writer = new ExcelObjectWriter();
        writer.excelObject.setName(name);
        writer.writeHandler(ExcelStyle.defaultCenterStyle());
        return writer;
    }

    public ExcelObjectWriter type(ExcelTypeEnum type) {
        this.excelObject.setType(type);
        return this;
    }

    public ExcelObjectWriter writeHandler(WriteHandler writeHandler) {
        this.excelObject.getWriteHandlers().add(writeHandler);
        return this;
    }


    public ExcelObjectWriter sheet(String name, Class<?> head, Collection<?> data) {
        this.excelObject.getSheets().add(new SheetObject(name, head, data));
        return this;
    }

    public ExcelObjectWriter sheet(String name, List<List<String>> head, Collection<?> data) {
        this.excelObject.getSheets().add(new SheetObject(name, head, data));
        return this;
    }

    public ExcelObjectWriter sheet(String name, Collection<?> data) {
        this.excelObject.getSheets().add(new SheetObject(name, data));
        return this;
    }

    public void doWrite() {
        ExcelWriteResolver.resolve(this.excelObject);
    }

    public void doWrite(OutputStream outputStream) {
        ExcelWriteResolver.resolve(this.excelObject, outputStream);
    }

    public void doWrite(File file) {
        ExcelWriteResolver.resolve(this.excelObject, file);
    }
}
