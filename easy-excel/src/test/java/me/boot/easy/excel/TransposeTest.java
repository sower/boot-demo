package me.boot.easy.excel;

import com.alibaba.excel.EasyExcel;
import java.util.ArrayList;
import java.util.List;
import me.boot.easy.excel.controller.ExcelObjectWriter;
import me.boot.easy.excel.controller.WebSite;
import me.boot.easy.excel.strategy.AutoFilterHandler;
import me.boot.easy.excel.strategy.AutoSequenceStrategy;
import me.boot.easy.excel.strategy.DropDownHandler;
import me.boot.easy.excel.strategy.FreezeHandler;
import me.boot.easy.excel.util.TransposeUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * TransposeTest
 *
 * @since 2023/09/24
 */
public class TransposeTest {

    ArrayList<WebSite> webSites = Lists.newArrayList(
        new WebSite("baidu", "123111111111111111111111"),
        new WebSite("bing", "123111111111111111111111"),
        new WebSite("bing", "123111111111111111111111"));

    public List<List<String>> data() {
        List<List<String>> transpose = TransposeUtils.transpose(webSites);
        transpose.add(Lists.newArrayList("head", "body", "body", "123111111111111111111111"));
        System.out.println(transpose);
        return transpose;
    }

    @Test
    public void test() {
        EasyExcel.write("test.xlsx")
            .sheet("adc")
//            .head(Lists.list(Lists.list("1"), Lists.list("3")))
            .head(WebSite.class)
//            .useDefaultStyle(false)
//            .registerWriteHandler(new MergeSameColumnStrategy())
//            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
//            .registerWriteHandler(new VerticalTableStyleStrategy())
//            .doWrite(data());
            .registerWriteHandler(new AutoSequenceStrategy())
            .registerWriteHandler(new DropDownHandler())
            .registerWriteHandler(new AutoFilterHandler())
            .registerWriteHandler(new FreezeHandler())
            .doWrite(webSites);

    }

    @Test
    public void excelObjectTest() {
        ExcelObjectWriter.excel("test")
            .sheet("adc", WebSite.class, webSites)
            .sheet("adc2", Lists.list(Lists.list("1", "11"), Lists.list("3")), webSites)
//            .writeHandlers(ListUtils.newArrayList(new LongestMatchColumnWidthStyleStrategy(),new FreezeHandler(),
//                new DropDownHandler()))
//        new AutoFilterHandler()))

//            .head(Lists.list(Lists.list("1","11"), Lists.list("3")));
            .doWrite();


    }

}
