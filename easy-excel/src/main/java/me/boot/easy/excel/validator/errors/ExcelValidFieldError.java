package me.boot.easy.excel.validator.errors;


import java.util.Collections;
import java.util.List;

public interface ExcelValidFieldError extends ExcelValidObjectError {


    /**
     * 获取列，从 1 开始
     */
    Integer getColumn();

    default List<String> getColumnNameList() {
        return Collections.emptyList();
    }

}
