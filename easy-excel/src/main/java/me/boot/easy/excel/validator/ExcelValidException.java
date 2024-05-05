package me.boot.easy.excel.validator;


import lombok.Getter;
import me.boot.easy.excel.validator.errors.ExcelValidErrors;

/**
 * 校验异常
 */
@Getter
public class ExcelValidException extends RuntimeException {

    private final ExcelValidErrors errors;

    public ExcelValidException(String message, ExcelValidErrors errors) {
        super(message);
        this.errors = errors;
    }

}
