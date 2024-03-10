package me.boot.httputil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.boot.httputil.config.FeignConfig;
import org.springframework.context.annotation.Import;

/**
 * EnableHttpBin
 *
 * @since 2024/03/09
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FeignConfig.class})
public @interface EnableHttpBin {

}
