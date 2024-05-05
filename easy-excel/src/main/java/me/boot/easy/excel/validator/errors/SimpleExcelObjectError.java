package me.boot.easy.excel.validator.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SimpleExcelObjectError implements ExcelValidObjectError {

    /**
     * 行号
     */
    private final Integer row;

    /**
     * 错误消息
     */
    private final String message;

}
