package me.boot.data.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * User
 *
 * @since 2024/05/26
 **/
@Data
@TableName(value = "t_user")
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String email;
}
