package me.boot.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * @description
 * @date 2023/06/14
 **/
public class GuavaTest {

    @Test
    public void testPrimitives() {
//        Set<String> strings = JSON.parseObject("[\"admin\"]",
//            new TypeReference<>() {
//            });
//        System.out.println(strings);
        System.out.println(JSON.toJSONString(Set.of("adc")));
    }

    public Type getType(Collection<?> set) {
        ParameterizedType genericSuperclass = (ParameterizedType) set.getClass()
            .getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        return actualTypeArguments[0];
    }


}
