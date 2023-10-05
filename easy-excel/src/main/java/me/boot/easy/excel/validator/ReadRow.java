package me.boot.easy.excel.validator;


public class ReadRow<T> {

    private final Integer rowIndex;

    private final T data;


    public ReadRow(Integer rowIndex, T data) {
        this.rowIndex = rowIndex;
        this.data = data;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ReadRow{" +
            "rowIndex=" + rowIndex +
            ", data=" + data +
            '}';
    }
}
