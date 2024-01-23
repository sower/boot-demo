package me.boot.httputil;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.dto.SingleResult;
import me.boot.httputil.service.HttpBinService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class HttpBinServiceTests {

    @Resource
    private HttpBinService httpBinService;


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
        JSONObject put = httpBinService.put(map,"9999");
        System.err.println(put);

        SingleResult<String> success = SingleResult.success(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ridiculus viverra iaculis class rutrum porta tortor consectetur hendrerit a aliquam. Suscipit rhoncus duis per suscipit enim quis morbi torquent pellentesque iaculis. Ipsum id montes pellentesque litora sociosqu ridiculus malesuada mi amet hendrerit. Lobortis pellentesque eleifend molestie sem scelerisque nisl faucibus duis ultrices massa. Lorem ante ullamcorper montes natoque vivamus vivamus lobortis ante montes leo. Condimentum mi cum class fermentum id tortor duis imperdiet aliquet sit.\n"
                + "\n"
                + "Cubilia nam tortor orci blandit arcu porttitor tellus urna platea hendrerit. Dictumst a elementum justo id praesent lacinia ridiculus amet mi et. Suspendisse mauris faucibus penatibus pharetra diam inceptos rutrum pulvinar consequat orci. Orci parturient nullam nunc non quis porta ante class inceptos praesent. Dapibus magnis natoque suscipit ornare habitasse mauris elit himenaeos morbi ad. Sodales proin dolor sagittis dignissim condimentum suscipit feugiat turpis sed pharetra. Viverra taciti dui mattis porttitor felis feugiat sollicitudin congue rutrum accumsan.");
        System.err.println(httpBinService.post(success));
    }

}
