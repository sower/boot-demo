package me.boot.easy.excel.strategy;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import java.util.Map;
import me.boot.easy.excel.property.ExcelDropDownProperty;
import me.boot.easy.excel.resolver.ExcelDropDownResolver;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * @description
 * @date 2023/09/29
 **/
public class DropDownHandler implements SheetWriteHandler {

    private Map<Integer, ExcelDropDownProperty> selectedMap;


    public DropDownHandler(
        Map<Integer, ExcelDropDownProperty> selectedMap) {
        this.selectedMap = selectedMap;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        if (selectedMap == null) {
            selectedMap = ExcelDropDownResolver.resolve(writeSheetHolder.getClazz());
        }

        DataValidationHelper helper = sheet.getDataValidationHelper();
        selectedMap.forEach((colIndex, property) -> {
            // 设置下拉列表的行： 首行，末行，首列，末列
            CellRangeAddressList rangeList = new CellRangeAddressList(property.getStartRow(),
                property.getEndRow(), colIndex, colIndex);
            // 设置下拉列表的值
            DataValidationConstraint constraint = helper.createExplicitListConstraint(
                property.getConstraints());
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
        });
    }
}