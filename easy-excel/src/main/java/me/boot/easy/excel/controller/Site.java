package me.boot.easy.excel.controller;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import me.boot.easy.excel.annotation.ExcelContent;

/**
 * @description
 * @date 2023/09/10
 **/
@Data
@ExcelIgnoreUnannotated
@ExcelContent(method = "getValue")
public class Site {


    @ExcelProperty("名称")
    private String name = "Ravi";

    @ExcelProperty("url")
    private String url;


    public Site(String name) {
        this.name = name;
    }

    public String getValue() {
        return name + "-" + url;
    }

}
