package me.boot.easy.excel.validator;

import java.util.ArrayList;
import java.util.List;
import me.boot.easy.excel.validator.errors.ExcelValidErrors;


public class CompositeExcelValidator implements ExcelValidator<Object> {

    private final List<ExcelValidator<Object>> validators;

    public CompositeExcelValidator(List<ExcelValidator<Object>> validators) {
        this.validators = new ArrayList<>(validators);
    }

    public boolean addValidator(ExcelValidator<Object> validator) {
        return this.validators.add(validator);
    }

    @Override
    public ExcelValidErrors validate(ReadTable<Object> readTable) {
        ExcelValidErrors errors = new ExcelValidErrors();
        for (ExcelValidator<Object> validator : this.validators) {
            errors.merge(validator.validate(readTable));
        }
        return errors;
    }
}
