package me.boot.datajpa.base;

import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import me.boot.datajpa.property.QueryCriteriaProperty;
import me.boot.datajpa.util.QueryPredicateUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * QuerySpecification
 *
 * @since 2023/12/26
 **/
public class QuerySpecification<T, Q> implements Specification<T> {

    private final Q queryBean;

    public QuerySpecification(Q queryBean) {
        this.queryBean = queryBean;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query,
        @NonNull CriteriaBuilder criteriaBuilder) {
        if (queryBean == null) {
            return criteriaBuilder.and();
        }
        List<QueryCriteriaProperty> properties = QueryPredicateUtils.getCriteriaProperties(
            queryBean);
        return criteriaBuilder.and(
            properties.stream()
                .map(property -> QueryPredicateUtils.toPredicate(root, criteriaBuilder, property))
                .filter(Objects::nonNull).toArray(Predicate[]::new));
    }

    public static <T, Q> Specification<T> of(@Nullable Q queryBean) {
        return new QuerySpecification<>(queryBean);
    }

}
