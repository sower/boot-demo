package me.boot.datajpa.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * json转换器
 *
 * @since 2023/12/14
 **/
@Converter
public abstract class JsonConverter<T> implements AttributeConverter<T, String> {

    private final TypeReference<T> typeReference;

    protected JsonConverter(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }


    @Override
    public String convertToDatabaseColumn(T object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        return JSON.toJSONString(object);
    }

    @Override
    public T convertToEntityAttribute(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, typeReference);
    }
}