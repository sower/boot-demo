package me.boot.easy.excel.controller;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.ListUtils;
import java.util.List;
import lombok.Data;
import me.boot.easy.excel.strategy.ObjectCollectionConverter;

/**
 * @description
 * @date 2023/09/10
 **/
@Data
@ExcelIgnoreUnannotated
public class WebSite {

    private int id = 0;

    @ExcelProperty("名称")
    private String name = "Ravi";

    @ExcelProperty("url")
    private String url;

//    @ExcelProperty("时间")
//    private LocalDateTime time = LocalDateTime.now();


    @ExcelProperty(value = "self", converter = ObjectCollectionConverter.class)
    private List<Site> self;


    public WebSite(String name, String url) {
        this.name = name;
        this.url = url;
        this.self = ListUtils.newArrayList(new Site(name + "1"), new Site(name + "2"));
    }
}
