package me.boot.web.mvc.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import java.text.Normalizer.Form;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;
import me.boot.base.util.DateUtils;
import me.boot.web.mvc.validation.SizePlus;
import org.hibernate.validator.constraints.Normalized;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 * MiscData
 *
 * @since 2024/04/14
 **/
@Data
@Schema(description = "混合数据")
public class MiscData {

    @URL
    @Schema(example = "https://boot.me")
    private String url;

    @PositiveOrZero
    @Digits(integer = 3, fraction = 2)
    @DecimalMax("999")
    private float floatNum;

    @NumberFormat(pattern = "#,###.00")
    private Double doubleNum;

    @PastOrPresent
    @DateTimeFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    @Schema(example = "2024-01-02 12:34:56")
    private LocalDateTime localDateTime;

    @UniqueElements
    @SizePlus(min = "1", max = "${validation.bin.max:3}")
    private List<@NotBlank @Normalized(form = Form.NFKD) String> list;

}
