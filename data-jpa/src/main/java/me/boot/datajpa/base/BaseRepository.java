package me.boot.datajpa.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Dao
 *
 * @date 2022/09/18
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> , JpaSpecificationExecutor<T> {

}
