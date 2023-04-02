package me.boot.httputil;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import me.boot.httputil.util.CloseHttpUtil;
import me.boot.httputil.util.OkHttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HttpUtilApplication.class)
class HttpUtilApplicationTests {

  Map<String, String> queryParams = ImmutableMap.of("query", "client");

  Map<String, String> headers = ImmutableMap.of("x-http", "https");

  @Test
  void closeHttpUtilTest() {
    String res = CloseHttpUtil.builder().url("http://httpbin.org/post")
        .headers(headers)
        .queryParams(queryParams)
        .body(headers)
        .build().post();
    System.out.println(res);
  }

  @Test
  void okHttpUtilTest() {
    String res = OkHttpUtil.builder().url("http://httpbin.org/post")
        .headers(headers)
        .queryParams(queryParams)
        .body(OkHttpUtil.jsonBody(headers))
        .build().post();
    System.out.println(res);
  }


}
