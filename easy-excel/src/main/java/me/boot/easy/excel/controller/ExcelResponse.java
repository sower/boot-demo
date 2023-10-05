package me.boot.easy.excel.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boot.easy.excel.strategy.ExcelStyle;

/**
 * @description
 * @date 2023/09/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelResponse {

    /**
     * 文件名称
     */
    @Builder.Default
    String name = "excel";

    /**
     * excel文件类型
     */
    @Builder.Default
    ExcelTypeEnum type = ExcelTypeEnum.XLSX;

    /**
     * sheet名称
     */
    @Builder.Default
    String sheetName = "sheet1";


    Class<?> headClass;

    List<List<String>> head;

    Collection<?> data;

    @Builder.Default
    Collection<WriteHandler> writeHandlers = Collections.singleton(ExcelStyle.defaultCenterStyle());

}
