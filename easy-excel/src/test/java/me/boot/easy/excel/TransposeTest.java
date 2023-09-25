package me.boot.easy.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import java.util.ArrayList;
import java.util.List;
import me.boot.easy.excel.controller.WebSite;
import me.boot.easy.excel.strategy.HorizontalHeaderStyleStrategy;
import me.boot.easy.excel.strategy.MergeSameColumnStrategy;
import me.boot.easy.excel.util.TransposeUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * @description
 * @date 2023/09/24
 **/
public class TransposeTest {

    public List<List<Object>> data() {
        ArrayList<WebSite> webSites = Lists.newArrayList(new WebSite("baidu", "baidu.com"),
            new WebSite("bing", "bing.cn"));

        List<List<Object>> transpose = TransposeUtil.transpose(webSites);
        transpose.add(Lists.newArrayList("head", "body", "body","123111111111111111111111"));
        System.out.println(transpose);
        return transpose;
    }

    @Test
    public void test() {
        EasyExcel.write("test.xlsx")
            .sheet("adc")
            .registerWriteHandler(new MergeSameColumnStrategy(0, Lists.newArrayList(1,2,3)))
            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
            .registerWriteHandler(new HorizontalHeaderStyleStrategy())
            .doWrite(data());
    }

}
