package me.boot.httputil;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.ImmutableMap;
import feign.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.dto.SingleResult;
import me.boot.httputil.service.HttpBinService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Slf4j
public class HttpBinServiceTests {

    @Resource
    private HttpBinService httpBinService;

    String filename = "application.yml";

    @Test
    public void testGet(){
        System.out.println(httpBinService.uuid());
    }

    @Test
    public void test(){
        ImmutableMap<String, ?> map = ImmutableMap.of("adc",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ridiculus viverra iaculis class rutrum porta tortor consectetur hendrerit a aliquam. Suscipit rhoncus duis per suscipit enim quis morbi torquent pellentesque iaculis. Ipsum id montes pellentesque litora sociosqu ridiculus malesuada mi amet hendrerit. Lobortis pellentesque eleifend molestie sem scelerisque nisl faucibus duis ultrices massa. Lorem ante ullamcorper montes natoque vivamus vivamus lobortis ante montes leo. Condimentum mi cum class fermentum id tortor duis imperdiet aliquet sit.\n"
                + "\n"
                + "Cubilia nam tortor orci blandit arcu porttitor tellus urna platea hendrerit. Dictumst a elementum justo id praesent lacinia ridiculus amet mi et. Suspendisse mauris faucibus penatibus pharetra diam inceptos rutrum pulvinar consequat orci. Orci parturient nullam nunc non quis porta ante class inceptos praesent. Dapibus magnis natoque suscipit ornare habitasse mauris elit himenaeos morbi ad. Sodales proin dolor sagittis dignissim condimentum suscipit feugiat turpis sed pharetra. Viverra taciti dui mattis porttitor felis feugiat sollicitudin congue rutrum accumsan.", "hj", 8);
        JSONObject put = httpBinService.put(map);
        System.err.println(put);

        SingleResult<String> success = SingleResult.success(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ridiculus viverra iaculis class rutrum porta tortor consectetur hendrerit a aliquam. Suscipit rhoncus duis per suscipit enim quis morbi torquent pellentesque iaculis. Ipsum id montes pellentesque litora sociosqu ridiculus malesuada mi amet hendrerit. Lobortis pellentesque eleifend molestie sem scelerisque nisl faucibus duis ultrices massa. Lorem ante ullamcorper montes natoque vivamus vivamus lobortis ante montes leo. Condimentum mi cum class fermentum id tortor duis imperdiet aliquet sit.\n"
                + "\n"
                + "Cubilia nam tortor orci blandit arcu porttitor tellus urna platea hendrerit. Dictumst a elementum justo id praesent lacinia ridiculus amet mi et. Suspendisse mauris faucibus penatibus pharetra diam inceptos rutrum pulvinar consequat orci. Orci parturient nullam nunc non quis porta ante class inceptos praesent. Dapibus magnis natoque suscipit ornare habitasse mauris elit himenaeos morbi ad. Sodales proin dolor sagittis dignissim condimentum suscipit feugiat turpis sed pharetra. Viverra taciti dui mattis porttitor felis feugiat sollicitudin congue rutrum accumsan.");
        System.err.println(httpBinService.post(success,"9999"));
    }
    @Test
    public void testStatus() {
//        System.err.println(httpBinService.delete("300"));
//        System.err.println(httpBinService.delete("429"));
//        System.err.println(httpBinService.delete("500"));
        System.err.println(httpBinService.get("adc", ImmutableMap.of()));
    }

    @Test
    public void testForm() throws IOException {
//        System.err.println(httpBinService.postFormUrl(ImmutableMap.of("size", "small")));
        System.err.println(httpBinService.postFormData(ImmutableMap.of("size", "small","adc","mate")));
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File(classloader.getResource(filename).getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",
            IOUtils.toByteArray(input));
        System.err.println(httpBinService.uploadFile(multipartFile));
    }

    @Test
    public void files() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File(classloader.getResource(filename).getFile());
        System.err.println(httpBinService.uploadFiles(Collections.singletonList(file)));

        Response response = httpBinService.image();
        System.err.println(response);
    }

}
