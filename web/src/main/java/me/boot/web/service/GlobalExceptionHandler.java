package me.boot.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.dto.SingleResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected SingleResult<?> handleBindException(BindException ex) {
        List<Map<String, Object>> list =
            ex.getFieldErrors().stream().map(this::fieldErrorToMap).collect(Collectors.toList());
        log.warn("bean validate exception: {}", ex.getMessage());
        return SingleResult.failure("Valid bean error: " + list);
    }

    /**
     * 普通参数(非 java bean)校验出错时抛出 ConstraintViolationException 异常
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Valid params failed: {}", ex.getMessage());
        return SingleResult.failure(ex.getMessage());
    }

    private Map<String, Object> fieldErrorToMap(FieldError error) {
        Map<String, Object> map = new HashMap<>();
        map.put("object", error.getObjectName());
        map.put("field", error.getField());
        map.put("rejectValue", error.getRejectedValue());
        map.put("message", error.getDefaultMessage());
        return map;
    }
}
