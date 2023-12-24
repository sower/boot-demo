package me.boot.datajpa.converter;

import java.util.List;
import javax.persistence.Converter;
import org.apache.commons.collections4.ListUtils;

/**
 * 字符串集合转换器
 *
 * @date 2022/10/02
 */
@Converter
public class StrListConverter extends JsonConverter<List<String>> {

    @Override
    public List<String> convertToEntityAttribute(String json) {
        return ListUtils.emptyIfNull(super.convertToEntityAttribute(json));
    }
}
