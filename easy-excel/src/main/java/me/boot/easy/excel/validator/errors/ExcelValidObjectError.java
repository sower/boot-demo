package me.boot.easy.excel.validator.errors;


public interface ExcelValidObjectError {

    /**
     * 获取行号，从 1 开始
     */
    Integer getRow();

    /**
     * 获取错误消息
     */
    String getMessage();
}
