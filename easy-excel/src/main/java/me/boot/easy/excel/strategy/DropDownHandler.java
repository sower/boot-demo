package me.boot.easy.excel.strategy;

import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.boot.easy.excel.annotation.ExcelDropDown;
import me.boot.easy.excel.property.ExcelDropDownProperty;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * DropDownHandler
 *
 * @since 2023/09/29
 */
@NoArgsConstructor
@AllArgsConstructor
public class DropDownHandler implements SheetWriteHandler {

    private Map<Integer, ExcelDropDownProperty> selectedMap;

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();

        ExcelWriteHeadProperty excelWriteHeadProperty = writeSheetHolder.getExcelWriteHeadProperty();
        if (selectedMap == null) {
            selectedMap = getDropDownMap(excelWriteHeadProperty);
        } else {
            checkAndSetDropDownMap(selectedMap, excelWriteHeadProperty.getHeadRowNumber());
        }

        selectedMap.forEach((colIndex, property) -> {
            buildDropDown(sheet, colIndex, property);
        });
    }

    private void buildDropDown(Sheet sheet, Integer colIndex,
        ExcelDropDownProperty property) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 设置下拉列表的值
        DataValidationConstraint constraint = helper.createExplicitListConstraint(
            property.getConstraints());
        // 设置下拉列表的行： 首行，末行，首列，末列
        CellRangeAddressList rangeList = new CellRangeAddressList(property.getStartRow(),
            property.getEndRow(), colIndex, colIndex);
        // 设置约束
        DataValidation dataValidation = helper.createValidation(constraint, rangeList);
        // 阻止输入非下拉选项的值
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setShowErrorBox(true);
        dataValidation.createErrorBox("非法输入", "请输入下拉选项中的内容");
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.createPromptBox("提示", "请选择下拉选型");
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    private Map<Integer, ExcelDropDownProperty> getDropDownMap(
        ExcelWriteHeadProperty headProperty) {
        if (!HeadKindEnum.CLASS.equals(headProperty.getHeadKind())) {
            return Collections.emptyMap();
        }

        Map<Integer, ExcelDropDownProperty> map = new HashMap<>(4);
        int headRowNumber = headProperty.getHeadRowNumber();
        headProperty.getHeadMap().forEach((index, head) -> {
            Field field = head.getField();
            ExcelDropDownProperty dropDownProperty = ExcelDropDownProperty.of(
                field.getAnnotation(ExcelDropDown.class));
            if (dropDownProperty == null) {
                return;
            }
            if (dropDownProperty.getStartRow() < 0) {
                dropDownProperty.setStartRow(headRowNumber);
            }
            map.put(index, dropDownProperty);
        });
        return map;
    }

    private void checkAndSetDropDownMap(Map<Integer, ExcelDropDownProperty> map,
        int headRowNumber) {
        map.forEach((index, property) -> {
            if (property.getStartRow() < 0) {
                property.setStartRow(headRowNumber);
            }
        });
    }
}