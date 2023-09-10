package me.boot.datajpa;

import java.util.List;
import javax.annotation.Resource;
import me.boot.datajpa.entity.User;
import me.boot.datajpa.repository.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

@SpringBootTest
class DataJpaApplicationTests {

    @Resource
    private UserDao userDao;

//    @Test
//    void init() {
//        User jack = new User().name("Lucy").gender(Gender.FEMALE).birth(LocalDate.of(2000, 9, 25))
//            .roles(Set.of("admin"));
//        User tom = new User().name("Alice");
//        userDao.saveAll(List.of(jack, tom));
//    }

    @Test
    void specificationQueryTest() {
        List<User> users = userDao.findAll((root, query, builder) -> builder.equal( root.get("online"),true));
        System.err.println(users);

//        users = userDao.findAll((root, query, builder) -> builder.isNull( root.get(User_.birth)));
//        System.err.println(users);
    }

    @Test
    void exampleQueryTest() {
        List<User> users = userDao.findAll(Example.of(new User().online(true)));
        System.err.println(users);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnorePaths("gender")
            .withIncludeNullValues()
            .withStringMatcher(StringMatcher.ENDING);

        Example<User> example = Example.of(new User().name("ck"), matcher);
        System.err.println(userDao.findAll(example));
    }

//    @Test
//    void dslQueryTest() {
//        QUser user = QUser.user;
//        Predicate predicate = user.name.contains("ac").or(user.roles.isNotEmpty());
//        System.err.println(userDao.findAll(predicate));
//    }
}
