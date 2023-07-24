package me.boot.web.bean.converter;

import com.alibaba.fastjson.JSON;
import java.util.Collections;
import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * @description
 * @date 2022/10/02
 */
@Converter
public class ListConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        return JSON.toJSONString(object);

    }

    @Override
    public Object convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) {
            return Collections.emptyList();
        }
        return JSON.parseArray(s);
    }
}
