package me.boot.httputil.service;

import com.alibaba.fastjson2.JSONObject;
import java.util.Map;
import me.boot.httputil.interceptor.TraceInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@EnableFeignClients
@FeignClient(name = "${boot.name}", url = "http://httpbin.org", configuration = TraceInterceptor.class)
public interface HttpBinService {

  @GetMapping("/uuid")
  Map<String, String> uuid();

  @GetMapping(value = "/get", headers = "app=test-app")
  JSONObject get(@RequestParam("msg") String msg, @RequestHeader Map<String, String> headers);

//  @PostMapping(
//      value = "/post",
//      headers = {"app=test-app", "test=demo"})
//  JSONObject post(@RequestBody Person person);

  @PutMapping(value = "/put")
  JSONObject put(@RequestBody Map map, @RequestHeader("Authorization") String token);

  @DeleteMapping(value = "/status/{code}")
  JSONObject delete(@PathVariable String code);
}
