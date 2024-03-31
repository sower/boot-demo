package me.boot.web.mvc.advice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.dto.SingleResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @date 2022/09/12
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generalExceptionHandler(Exception exception) {
        log.error("run exception", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * bean validate exception
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected SingleResult<?> handleBindException(BindException ex) {
        List<Map<String, Object>> list =
            ex.getFieldErrors().stream().map(this::fieldErrorToMap).collect(Collectors.toList());
        log.warn("bean validate exception: {}", ex.getMessage());
        return SingleResult.failure("Valid bean error: " + list);
    }

    private Map<String, Object> fieldErrorToMap(FieldError error) {
        Map<String, Object> map = new HashMap<>(6);
        map.put("object", error.getObjectName());
        map.put("field", error.getField());
        map.put("rejectValue", error.getRejectedValue());
        map.put("message", error.getDefaultMessage());
        return map;
    }

    /**
     * 普通参数(非 java bean)校验出错时抛出 ConstraintViolationException 异常
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public SingleResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Valid params failed: {}", ex.getMessage());
        return SingleResult.failure(ex.getMessage());
    }

    /**
     * http请求体格式转换异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public SingleResult<?> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex) {
        log.warn("Invalid Http message", ex);
        return SingleResult.failure(ex.getMessage());
    }


    /**
     * 请求参数缺失异常处理
     * MissingServletRequestParameterException
     */
    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SingleResult<?> handleMissingRequestException(ServletException ex) {
        log.warn("Request error: {}", ex.getMessage());
        return SingleResult.failure(ex.getMessage());
    }

//    @ExceptionHandler(JOSEException.class)
//    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
//    public SingleResult<?> handleHttpMessageNotReadableException(
//        HttpMessageNotReadableException ex) {
//        log.warn("Invalid Http message", ex);
//        return SingleResult.failure(ex.getMessage());
//    }

}
