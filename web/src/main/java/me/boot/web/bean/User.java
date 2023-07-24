package me.boot.web.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import me.boot.web.aspect.AnyOf;
import me.boot.web.bean.converter.ListConverter;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @description
 * @date 2022/09/28
 */
@Getter
@Setter
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name must be not blank")
    @Column(unique = true)
    private String name;

    private String password;

    @Lob
    @Convert(converter = ListConverter.class)
    private List<String> roles;

    @Positive
    @Max(200)
    private int age;

    @Transient
    private boolean online;

    @Past
//   后台到前台的时间格式的转换
    @JsonFormat(pattern = "yyyy-MM-dd")
    // 前后到后台的时间格式的转换
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private LocalDate birth;


    @AnyOf(
        values = {"man", "woman"},
        message = "only special values")
    private String gender;


    @Embedded
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
