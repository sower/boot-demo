package me.boot.easy.excel.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description
 * @date 2023/09/29
 **/
@Data
@NoArgsConstructor
public class ExcelObject {

    /**
     * 文件名称
     */
    String name = "excel";

    /**
     * excel文件类型
     */
    ExcelTypeEnum type = ExcelTypeEnum.XLSX;

    List<SheetObject> sheets = new ArrayList<>(2);

    List<WriteHandler> writeHandlers = new ArrayList<>(2);

}
