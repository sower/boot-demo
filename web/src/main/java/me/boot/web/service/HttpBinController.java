package me.boot.web.service;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.httputil.service.HttpBinService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * HttpBinController
 *
 * @since 2024/03/09
 **/
@Tag(name = "HttpBin", description = "By httpbin.org")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bin")
public class HttpBinController {

    private final HttpBinService httpBinService;

    @Operation(summary = "Generate UUID")
    @ApiResponse(responseCode = "200", description = "UUID")
    @GetMapping("uuid")
    public String uuid() {
        return httpBinService.uuid().get("uuid");
    }

    @Operation(summary = "文件上传")
    @PostMapping(value = "file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JSONObject uploadFile(@RequestPart("file") MultipartFile file,
        @RequestPart(required = false) String description) {
        log.info("receive file: {}, description: {}", file.getName(), description);
        return httpBinService.uploadFile(file);
    }

    @Operation(summary = "请求体")
    @PutMapping(value = "map")
    public JSONObject map(@RequestBody Map body) {
        return httpBinService.put(body);
    }

    @Operation(summary = "状态码", description = "HTTP 状态")
    @DeleteMapping(value = "status/{code}")
    public JSONObject status(
        @Parameter(description = "Status code", example = "200") @PathVariable String code) {
        return httpBinService.delete(code);
    }
}
