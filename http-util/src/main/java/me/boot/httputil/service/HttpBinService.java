package me.boot.httputil.service;

import com.alibaba.fastjson2.JSONObject;
import feign.Response;
import java.io.File;
import java.util.List;
import java.util.Map;
import me.boot.base.annotation.LogRecord;
import me.boot.base.dto.SingleResult;
import me.boot.httputil.interceptor.TraceInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "${boot.name}", url = "http://httpbin.org", configuration = TraceInterceptor.class)
public interface HttpBinService {

    @GetMapping("/uuid")
    Map<String, String> uuid();

    @LogRecord(content = "'record get ' + #root")
    @GetMapping(value = "/get", headers = {"app=test-app"})
    JSONObject get(@RequestParam("msg") String msg, @RequestHeader Map<String, String> headers);

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE)
    JSONObject post(@RequestBody SingleResult<?> body,
        @RequestHeader("Authorization") String token);

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    JSONObject postFormUrl(Map<String, ?> form);

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject postFormData(Map<String, ?> form);

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject uploadFile(@RequestPart(value = "file") MultipartFile file);

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject uploadFiles(@RequestPart(value = "files") List<File> file);

    @PutMapping(value = "/put")
    JSONObject put(@RequestBody Map map);

    @DeleteMapping(value = "/status/{code}")
    JSONObject delete(@PathVariable String code);

    @GetMapping(value = "/image/png")
    Response image();
}
