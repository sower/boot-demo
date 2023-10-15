package me.boot.easy.excel.spi;

import com.alibaba.excel.util.ListUtils;
import java.util.List;
import java.util.ServiceLoader;
import me.boot.easy.excel.resolver.WriteStrategyResolver;

/**
 * @description
 * @date 2023/10/13
 **/
public class ServiceLoaderUtil {

    public static List<WriteStrategyResolver> writeStrategyResolvers() {
        ServiceLoader<WriteStrategyResolver> loader = ServiceLoader.load(
            WriteStrategyResolver.class);
        return ListUtils.newArrayList(
            loader.iterator());
    }


    public static List<ExcelWriterHandler> excelWriterHandlers() {
        ServiceLoader<ExcelWriterHandler> loader = ServiceLoader.load(
            ExcelWriterHandler.class);
        return ListUtils.newArrayList(
            loader.iterator());
    }


    public static List<SheetWriterHandler> sheetWriterHandlers() {
        ServiceLoader<SheetWriterHandler> loader = ServiceLoader.load(
            SheetWriterHandler.class);
        return ListUtils.newArrayList(
            loader.iterator());
    }


}
