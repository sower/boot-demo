package me.boot.easy.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import java.util.ArrayList;
import java.util.List;
import me.boot.easy.excel.controller.WebSite;
import me.boot.easy.excel.strategy.HorizontalHeaderStyleStrategy;
import me.boot.easy.excel.strategy.HorizontalMergeStrategy;
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
        transpose.add(Lists.newArrayList("head", "body", "body"));
        System.out.println(transpose);
        return transpose;
//        System.out.println(CommonConverter.class.isAssignableFrom(ObjectCollectionConverter.class));
//        System.out.println(CommonConverter.class.isAssignableFrom(CommonConverter.class));
//        System.out.println(CommonConverter.class.isAssignableFrom(Converter.class));
    }

    @Test
    public void test() {
        EasyExcel.write("test.xlsx")
//            .head(head())
            .sheet("adc")
            .registerWriteHandler(new HorizontalMergeStrategy(0,new int[]{0,1,2,3}))
            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
            .registerWriteHandler(new HorizontalHeaderStyleStrategy())
//            .registerWriteHandler(ExcelStyle.centerDefaultStyle())
            .doWrite(data());
    }

}
