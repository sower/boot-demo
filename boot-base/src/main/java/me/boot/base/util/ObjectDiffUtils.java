package me.boot.base.util;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.boot.base.dto.FiledDifference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.NonNull;

/**
 * ObjectDiffUtils
 *
 * @since 2024/01/07
 **/
public abstract class ObjectDiffUtils {

    /**
     * Generates a list of differences between two objects.
     *
     * @param before the object before the changes
     * @param after  the object after the changes
     * @return a list of filed differences between the two objects
     */
    public static List<FiledDifference> diff(@NonNull Object before, @NonNull Object after) {
        Map<String, ?> beforeMap = (Map<String, ?>) BeanMap.create(before);
        Map<String, ?> afterMap = (Map<String, ?>) BeanMap.create(after);
        return diff(beforeMap, afterMap);
    }

    public static List<FiledDifference> diff(@NonNull Object before, @NonNull Object after,
        Collection<String> ignoreProperties) {
//        Map<String, ?> beforeMap =new HashMap<String, Object>(BeanMap.create(before)) ;
//        Map<String, ?> afterMap =  new HashMap<String, Object>(BeanMap.create(after)) ;

        Map<String, ?> beforeMap = JSONObject.from(before);
        Map<String, ?> afterMap = JSONObject.from(after);

        beforeMap = Maps.filterKeys(beforeMap, key -> !ignoreProperties.contains(key));
        afterMap = Maps.filterKeys(afterMap, key -> !ignoreProperties.contains(key));
//        for (String ignoreProperty : ignoreProperties) {
//            beforeMap.remove(ignoreProperty);
//            afterMap.remove(ignoreProperty);
//        }
        return diff(beforeMap, afterMap);
    }


    public static List<FiledDifference> diff(@NonNull Map<String, ?> before,
        @NonNull Map<String, ?> after) {
        MapDifference<String, Object> difference = Maps.difference(before, after);
        Map<String, ValueDifference<Object>> entriesDiffering = difference.entriesDiffering();
        return entriesDiffering.entrySet().stream().map(
            entry -> new FiledDifference(entry.getKey(), entry.getValue().leftValue(),
                entry.getValue().rightValue())).collect(Collectors.toList());
    }

}
