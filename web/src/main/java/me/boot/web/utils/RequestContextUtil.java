package me.boot.web.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextUtil {

    private static ServletRequestAttributes getRequestAttributes() {
        return
            (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    public static HttpServletRequest getContextRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getContextResponse() {
        return getRequestAttributes().getResponse();
    }

    public static HttpServletResponse getDownloadResponse(String fileName) {
        HttpServletResponse response = getContextResponse();
        String contentType = MediaTypeFactory.getMediaType(fileName)
            .map(MediaType::toString)
            .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
            "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        return response;
    }
}
