package me.boot.datajpa.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * QueryHelp
 *
 * @since 2023/12/24
 **/
@Slf4j
public abstract class QueryPredicateUtils {

    public static <R, Q> Predicate toPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        if (query == null) {
            return cb.and();
        }
        List<QueryCriteriaProperty> properties = getProperties(query);
        return cb.and(properties.stream().map(property -> toPredicate(root, cb, property))
            .filter(Objects::nonNull).toArray(Predicate[]::new));
    }

    private static <R> Predicate toPredicate(Root<R> root, CriteriaBuilder cb,
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
                List<?> between = new ArrayList<>((Collection<?>) property.getValue());
                if (between.size() == 2) {
                    return cb.between(root.get(property.getName()), (Comparable) between.get(0),
                        (Comparable) between.get(1));
                }
            default:
                return null;
        }
    }

    public static List<QueryCriteriaProperty> getProperties(Object query) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(query.getClass(),
            QueryCriteria.class);
        return fields.stream().map(field -> getProperty(query, field)).filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    public static QueryCriteriaProperty getProperty(Object query, Field field) {
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
