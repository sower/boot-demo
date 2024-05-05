package me.boot.easy.excel.validator;

import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class ReadTable<T> {

    private ExcelReadHeadProperty excelReadHeadProperty;

    private List<ReadRow<T>> rows;

    public ReadTable() {
        this.rows = new ArrayList<>();
    }

    public boolean isEmpty() {
        return rows == null || rows.isEmpty();
    }

}
