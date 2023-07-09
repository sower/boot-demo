package me.boot.web.repository;

import java.util.Optional;
import me.boot.web.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
}
