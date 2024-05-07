package me.boot.datajpa.criteria.core;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import me.boot.datajpa.criteria.util.QueryPredicateUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * QuerySpecification
 *
 * @since 2023/12/26
 **/
@AllArgsConstructor(staticName = "of")
public class QuerySpecification<T> implements Specification<T> {

    private final Object queryBean;

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query,
        @NonNull CriteriaBuilder criteriaBuilder) {
        return QueryPredicateUtils.toPredicate(root, criteriaBuilder, queryBean);
    }

}
