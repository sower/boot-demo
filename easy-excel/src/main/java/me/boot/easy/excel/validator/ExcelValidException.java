package me.boot.easy.excel.validator;


import me.boot.easy.excel.validator.errors.ExcelValidErrors;

/**
 * 校验异常
 */
public class ExcelValidException extends RuntimeException {

    private final ExcelValidErrors errors;

    public ExcelValidException(String message, ExcelValidErrors errors) {
        super(message);
        this.errors = errors;
    }

    public ExcelValidErrors getErrors() {
        return errors;
    }

}
