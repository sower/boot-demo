package me.boot.easy.excel.validator;


import me.boot.easy.excel.validator.errors.ExcelValidErrors;

/**
 * 数据校验
 */
public interface ExcelValidator<T> {

    /**
     * 校验
     *
     * @param readTable 读表
     */
    ExcelValidErrors validate(ReadTable<T> readTable);

}
