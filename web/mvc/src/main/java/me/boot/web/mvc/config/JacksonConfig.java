package me.boot.web.mvc.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import me.boot.base.util.DateUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 日期时间转换配置
 *
 * @since 2023/07/15
 **/
@Configuration
public class JacksonConfig {

    /**
     * 默认日期时间格式
     */
    private final String dateTimeFormat = DateUtils.DATE_TIME_FORMAT;
    /**
     * 默认日期格式 "yyyy-MM-dd"
     */
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    /**
     * 默认时间格式 "HH:mm:ss"
     */
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder ->
        {
            // 设置java.util.Date时间类的序列化以及反序列化的格式
            builder.simpleDateFormat(dateTimeFormat);

            // JSR 310日期时间处理
            JavaTimeModule javaTimeModule = new JavaTimeModule();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(dateTimeFormatter));
            javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(dateTimeFormatter));

            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
            javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(dateFormatter));

            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
            javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(timeFormatter));

            builder.modules(javaTimeModule);

        };
    }
}
