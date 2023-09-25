package me.boot.easy.excel.handler;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import me.boot.easy.excel.annotation.ExcelMergeColumn;
import me.boot.easy.excel.annotation.ResponseExcel;
import me.boot.easy.excel.strategy.AutoColumnWidthStrategy;
import me.boot.easy.excel.strategy.ExcelMergeStrategy;
import me.boot.easy.excel.strategy.ExcelStyle;
import org.springframework.http.HttpHeaders;

/**
 * @description
 * @date 2023/09/10
 **/
public class ExcelWriteHandler {

    @SneakyThrows(IOException.class)
    public <T> void write(List<T> returnValue, ResponseExcel responseExcel,
        HttpServletResponse response) {
        // 文件名
        response.setContentType("application/vnd.ms-excel");
        String fileName = URLEncoder.encode(responseExcel.name(),
                StandardCharsets.UTF_8)+ responseExcel.suffix().getValue();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename*=utf-8''" + fileName);
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcelFactory.write(
                response.getOutputStream(), returnValue.get(0).getClass())
            .excelType(responseExcel.suffix()).sheet(responseExcel.sheetName())
            .registerWriteHandler(new AutoColumnWidthStrategy())
            .registerWriteHandler(ExcelStyle.defaultCenterStyle());

        if (responseExcel.mergeColumn().length > 0) {
            excelWriterSheetBuilder.registerWriteHandler(getMergeStrategy(responseExcel, returnValue));
        }
        excelWriterSheetBuilder.doWrite(returnValue);
    }

    private <T> ExcelMergeStrategy getMergeStrategy(ResponseExcel responseExcel,
        List<T> returnValue) {
        Class<?> dataClass = returnValue.get(0).getClass();
        List<Field> fieldList = new ArrayList<>();
        while (dataClass != null) {
            Collections.addAll(fieldList, dataClass.getDeclaredFields());
            // Get the parent class and give it to yourself
            dataClass = dataClass.getSuperclass();
        }
        Field mergeField = fieldList.stream().filter(field -> field.getAnnotation(
                ExcelMergeColumn.class) != null).findFirst()
            .orElseThrow(() -> new RuntimeException("excel合并未找到ExcelMergeColumn注解"));
        Map<Object, List<T>> mergeMap = returnValue.stream().collect(Collectors.groupingBy(e -> {
            Object value = null;
            try {
                mergeField.setAccessible(true);
                value = mergeField.get(e);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            return value;
        }));

        // 要合并的行，key：起始行，value：结束行
        Map<Integer, Integer> mapMerge = new HashMap<>();
        // 要合并的起始行 = head的行数
        int index = responseExcel.headNumber();
        // index == -1 没有指定起始行
        if (index == -1) {
            ExcelProperty excelProperty = Optional.of(mergeField.getAnnotation(ExcelProperty.class))
                .orElseThrow(() -> new RuntimeException("excel合并列未找到ExcelProperty注解"));
            index = excelProperty.value().length;
        }
        // 组后数据的顺序会变化，清空数据 计算合并时从新添加分
        returnValue.clear();
        for (List<T> vos : mergeMap.values()) {
            returnValue.addAll(vos);
            mapMerge.put(index, index + vos.size() - 1);
            index += vos.size();
        }
        return new ExcelMergeStrategy(mapMerge, responseExcel.mergeColumn());
    }

}
