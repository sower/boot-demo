package me.boot.easy.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * @description
 * @date 2023/09/29
 **/
public class CellUtil {

    public static Object getCellValue(Cell cell) {
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
}
