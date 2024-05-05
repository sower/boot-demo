package me.boot.easy.excel.validator;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadRow<T> {

    private Integer rowIndex;

    private T data;

}
