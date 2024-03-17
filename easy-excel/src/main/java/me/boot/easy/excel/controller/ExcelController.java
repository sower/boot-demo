package me.boot.easy.excel.controller;

import com.alibaba.excel.util.ListUtils;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import me.boot.easy.excel.annotation.ExcelParam;
import me.boot.easy.excel.validator.errors.ExcelValidErrors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2022/10/02
 */
@RestController
@Slf4j
@Validated
public class ExcelController {

    @GetMapping("excel")
    public ExcelObject exportTest() {
        ArrayList<WebSite> webSites = ListUtils.newArrayList(new WebSite("baidu", "baidu.com"),
            new WebSite("bing", "bing.cn"));

        return new ExcelObject();
    }

    @PostMapping("excel")
    public List<WebSite> importExcel(@ExcelParam @Valid List<WebSite> list) {
        return list;
    }

    @PostMapping("/excel/all")
    public List<?> listObj(@ExcelParam @Validated @Size(max = 10) List<WebSite> list,
        ExcelValidErrors errors) {
        return ListUtils.newArrayList(list, errors.getAllErrors());
    }

}
