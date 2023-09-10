package me.boot.web.controller;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.dto.SingleResult;
import me.boot.datajpa.entity.User;
import me.boot.datajpa.repository.UserDao;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2022/10/02
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Resource
    private UserDao userDao;

    @GetMapping
    public List<User> queryUsers() {
        return userDao.findAll();
    }

    @GetMapping("/{id}")
    public User queryUser(@PathVariable String id) {
        return userDao.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody @Validated User user) {
        userDao.save(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated User user) {
        userDao.save(user);
        return user;
    }

    @DeleteMapping
    public SingleResult<?> deleteUser(String id) {
        userDao.deleteById(id);
        return SingleResult.success();
    }


}
