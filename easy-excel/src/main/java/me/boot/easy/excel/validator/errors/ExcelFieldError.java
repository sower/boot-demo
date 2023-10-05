package me.boot.easy.excel.validator.errors;


import com.alibaba.excel.metadata.Head;
import java.util.List;
import org.springframework.validation.FieldError;

public class ExcelFieldError extends FieldError implements ExcelValidFieldError {

    private final Integer row;

    private final Integer column;

    private final List<String> columnNameList;

    public ExcelFieldError(FieldError fieldError, Integer row, Integer column,
        List<String> columnNameList) {
        super(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
            fieldError.isBindingFailure(), fieldError.getCodes(), fieldError.getArguments(),
            fieldError.getDefaultMessage());
        this.row = row;
        this.column = column;
        this.columnNameList = columnNameList;
    }

    public ExcelFieldError(FieldError fieldError, Integer row, Head head) {
        this(fieldError, row, head.getColumnIndex() + 1, head.getHeadNameList());
    }

    @Override
    public Integer getRow() {
        return row;
    }

    @Override
    public String getMessage() {
        return getDefaultMessage();
    }

    @Override
    public Integer getColumn() {
        return column;
    }

    @Override
    public List<String> getColumnNameList() {
        return columnNameList;
    }
}
