package me.boot.datajpa.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.boot.datajpa.annotation.QueryCriteria;
import me.boot.datajpa.property.QueryCriteriaProperty;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * QueryHelper
 *
 * @since 2023/12/24
 **/
@Slf4j
public abstract class QueryPredicateUtils {

    /**
     * 转换为查询组合谓词
     *
     * @param root      /
     * @param cb        /
     * @param queryBean 查询条件对象
     * @return {@link Predicate}
     */
    public static <T> Predicate toPredicate(Root<T> root, CriteriaBuilder cb, Object queryBean) {
        if (queryBean == null) {
            return cb.and();
        }
        List<QueryCriteriaProperty> properties = getCriteriaProperties(queryBean);
        return cb.and(properties.stream().map(property -> toPredicate(root, cb, property))
            .filter(Objects::nonNull).toArray(Predicate[]::new));
    }

    /**
     * 转换为单个查询谓词
     *
     * @param root     /
     * @param cb       /
     * @param property 查询条件属性
     * @return {@link Predicate}
     */
    public static <T> Predicate toPredicate(Root<T> root, CriteriaBuilder cb,
        QueryCriteriaProperty property) {
        switch (property.getOperation()) {
            case EQUAL:
                return cb.equal(root.get(property.getName()), property.getValue());
            case NOT_EQUAL:
                return cb.notEqual(root.get(property.getName()), property.getValue());
            case LIKE:
                return cb.like(root.get(property.getName()), "%" + property.getValue() + "%");
            case LEFT_LIKE:
                return cb.like(root.get(property.getName()), "%" + property.getValue());
            case RIGHT_LIKE:
                return cb.like(root.get(property.getName()), property.getValue() + "%");
            case IN:
                return root.get(property.getName()).in((Collection<?>) property.getValue());
            case NOT_IN:
                return root.get(property.getName()).in((Collection<?>) property.getValue()).not();
            case NOT_NULL:
                return cb.isNotNull(root.get(property.getName()));
            case IS_NULL:
                return cb.isNull(root.get(property.getName()));
            case GREATER_THAN:
                return cb.greaterThanOrEqualTo(root.get(property.getName())
                        .as((Class<? extends Comparable>) property.getClazz()),
                    (Comparable) property.getValue());
            case LESS_THAN:
                return cb.lessThanOrEqualTo(root.get(property.getName()),
                    (Comparable) property.getValue());
            case LESS_THAN_NQ:
                return cb.lessThan(root.get(property.getName()), (Comparable) property.getValue());
            case BETWEEN:
                Collection<?> between = (Collection<?>) property.getValue();
                if (between.size() == 2) {
                    return cb.between(root.get(property.getName()),
                        (Comparable) IterableUtils.get(between, 0),
                        (Comparable) IterableUtils.get(between, 1));
                }
            default:
                log.warn("not support operation: {}", property.getOperation());
                return null;
        }
    }

    /**
     * 获取规格属性列表
     *
     * @param queryBean 查询条件对象
     * @return {@link List}<{@link QueryCriteriaProperty}>
     */
    public static List<QueryCriteriaProperty> getCriteriaProperties(Object queryBean) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(queryBean.getClass(),
            QueryCriteria.class);
        return fields.stream().map(field -> toCriteriaProperty(queryBean, field))
            .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SneakyThrows
    private static QueryCriteriaProperty toCriteriaProperty(Object query, Field field) {
        Object value = FieldUtils.readField(field, query, true);
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        QueryCriteria queryCriteria = field.getAnnotation(QueryCriteria.class);
        String queryKey = StringUtils.defaultIfBlank(queryCriteria.name(), field.getName());
        return new QueryCriteriaProperty(queryKey, queryCriteria.operation(), value,
            field.getType());
    }

}
