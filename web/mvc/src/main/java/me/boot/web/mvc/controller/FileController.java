package me.boot.web.mvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import me.boot.web.mvc.bean.FileData;
import me.boot.web.mvc.bean.MiscData;
import me.boot.web.mvc.util.RequestContextUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器
 *
 * @since 2024/03/09
 **/
@Tag(name = "文件控制器")
@Slf4j
@Validated
@RestController
@RequestMapping("/files")
public class FileController {

    @Operation(summary = "文件上传")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(@RequestParam("files") List<MultipartFile> files,
        @RequestParam(required = false) String description) {
        log.info("receive {} file", files.size());
        return "OK";
    }

    @Operation(summary = "文件上传-Part")
    @PostMapping(value = "part", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPart(@RequestPart("file") MultipartFile file,
        @RequestPart(required = false) MiscData miscData) {
        log.info("misc data: {}", miscData);
        if (file.isEmpty()) {
            return "File is Empty";
        }
        return "OK";
    }

    @Operation(summary = "文件及数据上传")
    @PostMapping(value = "data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(FileData data) {
        log.info("receive file data {} ", data);
        MultipartFile file = data.getFile();
        return file.getOriginalFilename();
    }

    @Operation(summary = "文件下载", description = "支持断点续传")
    @GetMapping("/{fileName}")
    public void download(@PathVariable @NotBlank String fileName) {
        Resource resource = new ClassPathResource("classpath:" + fileName);
        HttpServletResponse response = RequestContextUtils.getDownloadResponse(
            resource.getFilename());
        HttpServletRequest request = RequestContextUtils.getContextRequest();
        String ranges = request.getHeader(HttpHeaders.RANGE);
        List<HttpRange> httpRanges = HttpRange.parseRanges(ranges);
        if (httpRanges.isEmpty()) {
            doDownload(response, resource);
            return;
        }
        rangeDownload(response, resource, httpRanges);
    }

    private void doDownload(HttpServletResponse response, Resource resource) {
        try (ServletOutputStream outputStream = response.getOutputStream(); InputStream inputStream = resource.getInputStream()) {
            response.setHeader(HttpHeaders.CONTENT_LENGTH,
                String.valueOf(resource.contentLength()));
            IOUtils.copyLarge(inputStream, outputStream);
        } catch (IOException e) {
            log.error("Failed to download file");
            throw new RuntimeException(e);
        }
    }

    private void rangeDownload(HttpServletResponse response, Resource resource,
        List<HttpRange> httpRanges) {
        List<ResourceRegion> resourceRegions = HttpRange.toResourceRegions(httpRanges, resource);
        try {
            rangeDownload(response, resourceRegions.get(0));
        } catch (IOException e) {
            log.error("Failed to download file");
            throw new RuntimeException(e);
        }
    }

    private void rangeDownload(HttpServletResponse response, ResourceRegion resourceRegion)
        throws IOException {
        // <unit> <range-start>-<range-end>/<size>
        long start = resourceRegion.getPosition();
        long end = start + resourceRegion.getCount();
        Resource resource = resourceRegion.getResource();
        String contentRange = String.format("bytes %d-%d/%d", start, end, resource.contentLength());
        response.setHeader(HttpHeaders.CONTENT_RANGE, contentRange);
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
        doDownload(response, resource);
    }

}
