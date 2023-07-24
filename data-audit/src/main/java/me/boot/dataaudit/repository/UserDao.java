package me.boot.dataaudit.repository;

import java.util.Optional;
import me.boot.dataaudit.base.BaseRepository;
import me.boot.dataaudit.entity.User;
import org.javers.spring.annotation.JaversAuditable;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
public interface UserDao extends BaseRepository<User, Integer> {

    Optional<User> findByName(String name);

}
