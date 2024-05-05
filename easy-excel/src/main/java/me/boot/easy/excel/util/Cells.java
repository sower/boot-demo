package me.boot.easy.excel.util;

import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 单元格工具
 *
 * @since 2023/09/29
 **/
public abstract class Cells {

    @Nullable
    public static Object getValue(@NonNull Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericCellValue = cell.getNumericCellValue();
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateUtil.getLocalDateTime(numericCellValue);
                } else {
                    return numericCellValue;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return CellValue.getError(cell.getErrorCellValue()).formatAsString();
            default:
                return null;
        }
    }

    public static boolean equals(@NonNull Cell cell1, @NonNull Cell cell2) {
        return Objects.equals(getValue(cell1), getValue(cell2));
    }
}
