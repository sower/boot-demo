package me.boot.datajpa;

import com.google.common.collect.ImmutableList;
import com.zaxxer.hikari.HikariConfig;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.boot.base.constant.Operation;
import me.boot.datajpa.annotation.QueryCriteria;
import me.boot.datajpa.base.QuerySpecification;
import me.boot.datajpa.constant.Gender;
import me.boot.datajpa.entity.User;
import me.boot.datajpa.entity.WebSite;
import me.boot.datajpa.repository.UserDao;
import me.boot.datajpa.repository.WebSiteDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

@SpringBootTest
class DataJpaApplicationTests {

    @Resource
    private UserDao userDao;

    @Resource
    private WebSiteDao webSiteDao;

    @Resource
    private HikariConfig hikariConfig;

    @Test
    public void config() {
        System.err.println(hikariConfig.getMaximumPoolSize());
        System.err.println(hikariConfig.getDataSourceProperties());
    }

    @Test
    void init() {
        User jack = new User().name("Lucy").gender(Gender.FEMALE).birth(LocalDate.of(2000, 9, 25))
            .roles(ImmutableList.of("admin"));
        User tom = new User().name("Alice");
        userDao.saveAll(ImmutableList.of(jack, tom));
    }

    @Test
    void queryWebSite() {

        WebSite jack = webSiteDao.save(
            new WebSite().version("1.0").name("jack").url("https://www.jack.com"));
        webSiteDao.save(new WebSite().version("2.0").name("bing").url("https://www.bing.com"));
//        webSiteDao.findAllByName("jack").forEach(System.err::println);
//        webSiteDao.findById(new VersionId("1.0", "1.0")).ifPresent(System.err::println);
//        List<WebSite> bing = webSiteDao.findAllByNameAndVersion("bing", "2.0");
//        webSiteDao.deleteAll(bing);
//        System.err.println(webSiteDao.findAll());
    }

    @Test
    void predicateQuery() {
        userDao.findAll(QuerySpecification.of(new QueryBean(null, Gender.UNKNOWN)))
            .forEach(System.err::println);
        webSiteDao.findAll(QuerySpecification.of(new QueryBean("jac", null)))
            .forEach(System.err::println);
    }

    @Test
    void specificationQueryTest() {
        List<User> users = userDao.findAll(
            (root, query, builder) -> builder.equal(root.get("online"), true));
        System.err.println(users);
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

    @Data
    @AllArgsConstructor
    static class QueryBean {

        @QueryCriteria(operation = Operation.RIGHT_LIKE)
        private String name;

        @QueryCriteria
        private Gender gender;
    }
}
