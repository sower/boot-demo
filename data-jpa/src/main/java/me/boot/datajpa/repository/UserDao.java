package me.boot.datajpa.repository;

import java.util.Collection;
import java.util.Optional;
import me.boot.datajpa.base.BaseRepository;
import me.boot.datajpa.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDao extends BaseRepository<User, String> {

    Optional<User> findByName(String name);

    @Modifying
    @Query("update User u set u.name=:name where u.id=:id")
    void updateUserName(@Param("id") String id, @Param("name") String name);

    void deleteByName(String name);

    <T> Collection<T> findByOnline(boolean online, Class<T> type);

}
