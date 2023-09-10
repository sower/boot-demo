package me.boot.datajpa.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 字符串集合转换器
 *
 * @date 2022/10/02
 */
@Converter
public class StrListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if (CollectionUtils.isEmpty(strings)) {
            return StringUtils.EMPTY;
        }
        return JSON.toJSONString(strings);

    }

    @Override
    public List<String> convertToEntityAttribute(String str) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();
        }
        return JSON.parseObject(str, new TypeReference<>() {
        });
    }
}
