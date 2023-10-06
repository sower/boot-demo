package me.boot.easy.excel.validator.errors;


public class SimpleExcelValidFieldError extends SimpleExcelObjectError implements
    ExcelValidFieldError {

    private final Integer column;

    public SimpleExcelValidFieldError(Integer row, Integer column, String message) {
        super(row, message);
        this.column = column;
    }

    @Override
    public Integer getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "DefaultExcelValidFieldError{" +
            "column=" + column +
            '}';
    }
}
