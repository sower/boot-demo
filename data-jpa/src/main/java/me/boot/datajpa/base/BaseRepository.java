package me.boot.datajpa.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base Dao
 *
 * @since 2022/09/18
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> , AutoSpecificationExecutor<T> {

}
