package me.boot.easy.excel.validator.errors;


import org.springframework.validation.ObjectError;

public class ExcelObjectError extends ObjectError implements ExcelValidObjectError {

    /**
     * 行号
     */
    private final Integer row;

    public ExcelObjectError(ObjectError objectError, Integer row) {
        super(objectError.getObjectName(), objectError.getCodes(), objectError.getArguments(),
            objectError.getDefaultMessage());
        this.row = row;
    }

    @Override
    public Integer getRow() {
        return row;
    }

    @Override
    public String getMessage() {
        return getDefaultMessage();
    }

}
