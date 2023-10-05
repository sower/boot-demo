package me.boot.easy.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @description
 * @date 2023/09/24
 **/
public interface CommonConverter<T> extends Converter<T> {

    @Override
    default WriteCellData<?> convertToExcelData(T value, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {

        return new WriteCellData<String>(convertToExcelData(value));
    }

    default String convertToExcelData(T value) {
        throw new UnsupportedOperationException(
            "The current operation is not supported by the current converter.");
    }
}
