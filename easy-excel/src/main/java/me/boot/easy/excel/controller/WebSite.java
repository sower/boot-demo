package me.boot.easy.excel.controller;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.ListUtils;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boot.easy.excel.annotation.AutoCellSize;
import me.boot.easy.excel.annotation.AutoFilter;
import me.boot.easy.excel.annotation.ExcelDropDown;
import me.boot.easy.excel.annotation.FreezePane;
import me.boot.easy.excel.annotation.MergeSameRow;
import me.boot.easy.excel.converter.ObjectCollectionConverter;

/**
 * @description
 * @date 2023/09/10
 **/
@Data
@ExcelIgnoreUnannotated
@NoArgsConstructor
@FreezePane
@AutoFilter
@AutoCellSize
@MergeSameRow
public class WebSite {

    private int id = 0;

    @ExcelProperty("名称")
    @ExcelDropDown(values = {"baidu", "bing"})
    @NotBlank
    private String name;

    @ExcelProperty("url")
    @Size(max = 8)
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
