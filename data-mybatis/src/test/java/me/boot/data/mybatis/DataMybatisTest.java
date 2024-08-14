package me.boot.data.mybatis;

import java.util.List;
import me.boot.data.mybatis.entity.User;
import me.boot.data.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * DataMybatisTest
 *
 * @since 2024/05/26
 **/
@SpringBootTest
public class DataMybatisTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assertions.assertEquals(5, userList.size());
        userList.forEach(System.out::println);

        User user = userMapper.selectById(3);
        System.err.println(user);
    }

}
