package me.boot.datajpa.base;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

/**
 * AutoSpecificationExecutor
 *
 * @since 2024/04/29
 **/
public interface AutoSpecificationExecutor<T> extends JpaSpecificationExecutor<T> {

    default Optional<T> findOne(@Nullable Object object) {
        return findOne(QuerySpecification.of(object));
    }

    default List<T> findAll(@Nullable Object object) {
        return findAll(QuerySpecification.of(object));
    }


    default Page<T> findAll(@Nullable Object object, Pageable pageable) {
        return findAll(QuerySpecification.of(object), pageable);
    }

    default List<T> findAll(@Nullable Object object, Sort sort) {
        return findAll(QuerySpecification.of(object), sort);
    }

    default long count(@Nullable Object object) {
        return count(QuerySpecification.of(object));
    }

    default boolean exists(Object object) {
        return exists(QuerySpecification.of(object));
    }
}
