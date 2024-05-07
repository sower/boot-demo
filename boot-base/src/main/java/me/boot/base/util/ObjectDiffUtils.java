package me.boot.base.util;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import me.boot.base.dto.FiledDifference;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
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
        return diff(before, after, Collections.emptyList());
    }

    public static <T> List<FiledDifference> diff2(@NonNull T before, @NonNull T after) {
        return diff2(before, after, Collections.emptyList());
    }

    public static <T> List<FiledDifference> diff2(@NonNull T before, @NonNull T after,
        Collection<String> ignoreProperties) {
        String[] array = ignoreProperties.toArray(new String[0]);
        return new ReflectionDiffBuilder<>(before, after,
            ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames(array).build().getDiffs()
            .stream()
            .map(item -> new FiledDifference(item.getFieldName(), item.getLeft(), item.getRight()))
            .collect(Collectors.toList());
    }


    public static List<FiledDifference> diff(@NonNull Object before, @NonNull Object after,
        Collection<String> ignoreProperties) {
        if (!CommonUtils.isAllJavaBean(before, after)) {
            throw new IllegalArgumentException("The two objects must be JavaBean");
        }
        // Map<String, ?> beforeMap = (Map<String, ?>) BeanMap.create(before);
        // Map<String, ?> afterMap = (Map<String, ?>) BeanMap.create(after);

        Map<String, ?> beforeMap = JSONObject.from(before);
        Map<String, ?> afterMap = JSONObject.from(after);

        beforeMap = Maps.filterKeys(beforeMap, key -> !ignoreProperties.contains(key));
        afterMap = Maps.filterKeys(afterMap, key -> !ignoreProperties.contains(key));
        // for (String ignoreProperty : ignoreProperties) {
        //     beforeMap.remove(ignoreProperty);
        //     afterMap.remove(ignoreProperty);
        // }
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


    /**
     * 同类对象集合差异比对
     *
     * @param before 旧值
     * @param after  新值
     * @return 差异列表对
     */
    public static <T> Pair<List<Object>, List<Object>> diffList(@NonNull Collection<T> before,
        @NonNull Collection<T> after, String key) {
        // 以相同标识对比
        ImmutableMap<Object, T> beforeMap = Maps.uniqueIndex(before,
            item -> getIdentifier(key, item));
        ImmutableMap<Object, T> afterMap = Maps.uniqueIndex(after,
            item -> getIdentifier(key, item));
        MapDifference<Object, Object> difference = Maps.difference(beforeMap, afterMap);

        List<Object> oldList = new LinkedList<>();
        List<Object> newList = new LinkedList<>();
        difference.entriesDiffering().entrySet().stream().map(entry -> buildDiffPair(key, entry))
            .forEach(pair -> {
                oldList.add(pair.getLeft());
                newList.add(pair.getRight());
            });
        oldList.addAll(difference.entriesOnlyOnLeft().values());
        newList.addAll(difference.entriesOnlyOnRight().values());
        return Pair.of(oldList, newList);
    }

    private static Pair<Map<String, Object>, Map<String, Object>> buildDiffPair(String key,
        Entry<Object, ValueDifference<Object>> entry) {
        ValueDifference<Object> value = entry.getValue();
        Pair<Map<String, Object>, Map<String, Object>> diffPair = getDiffPair(value.leftValue(),
            value.rightValue());
        // 反填标识符
        Map<String, Object> left = new HashMap<>(diffPair.getLeft());
        left.put(key, entry.getKey());
        Map<String, Object> right = new HashMap<>(diffPair.getRight());
        right.put(key, entry.getKey());
        return Pair.of(left, right);
    }

    @SneakyThrows
    private static <T> Object getIdentifier(String key, T item) {
        return FieldUtils.readField(item, key, true);
    }

    private static Pair<Map<String, Object>, Map<String, Object>> getDiffPair(
        @NonNull Object before, @NonNull Object after) {
        if (!CommonUtils.isAllJavaBean(before, after)) {
            throw new IllegalArgumentException("The two objects must be JavaBean");
        }
        Map<String, Object> beforeMap = JSONObject.from(before);
        Map<String, Object> afterMap = JSONObject.from(after);
        MapDifference<String, Object> difference = Maps.difference(beforeMap, afterMap);
        Set<String> properties = difference.entriesDiffering().keySet();
        beforeMap = Maps.filterKeys(beforeMap, properties::contains);
        afterMap = Maps.filterKeys(afterMap, properties::contains);
        return Pair.of(beforeMap, afterMap);
    }

}
