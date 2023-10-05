package me.boot.easy.excel.validator;

import com.alibaba.excel.metadata.Head;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.boot.easy.excel.validator.errors.ExcelFieldError;
import me.boot.easy.excel.validator.errors.ExcelObjectError;
import me.boot.easy.excel.validator.errors.ExcelValidErrors;
import me.boot.easy.excel.validator.errors.ExcelValidObjectError;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

/**
 * 默认excel校验器
 */
public class DefaultExcelValidator implements ExcelValidator<Object> {

    private final Validator validator;

    public DefaultExcelValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public ExcelValidErrors validate(ReadTable<Object> readTable) {
        if (readTable.isEmpty()) {
            return new ExcelValidErrors(Collections.emptyList());
        }
        Map<String, Head> fieldHeadMap = readTable.getExcelReadHeadProperty().getHeadMap().values()
            .stream().collect(Collectors.toMap(Head::getFieldName, head -> head));
        List<ReadRow<Object>> rows = readTable.getRows();

        List<ExcelValidObjectError> validObjectErrors =
            rows.stream().map(row -> {
                Object data = row.getData();
                Errors errors = new BeanPropertyBindingResult(data, "data");
                this.validator.validate(data, errors);
                return getValidErrors(fieldHeadMap, row.getRowIndex(), errors);
            }).flatMap(Collection::stream).collect(Collectors.toList());
        return new ExcelValidErrors(validObjectErrors);
    }

    private List<ExcelValidObjectError> getValidErrors(Map<String, Head> fieldHeadMap,
        Integer rowIndex, Errors errors) {
        return errors.getAllErrors()
            .stream().map(error -> {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    Head head = fieldHeadMap.get(fieldError.getField());
                    return new ExcelFieldError(fieldError, rowIndex + 1, head);
                }
                return new ExcelObjectError(error, rowIndex + 1);
            }).collect(Collectors.toList());
    }
}
