package me.boot.easy.excel.model;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Excel对象
 *
 * @since 2023/09/29
 */
@Data
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
