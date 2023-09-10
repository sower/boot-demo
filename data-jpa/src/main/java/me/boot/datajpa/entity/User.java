package me.boot.datajpa.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.boot.datajpa.base.AuditEntity;
import me.boot.datajpa.constant.Gender;
import me.boot.datajpa.converter.StrListConverter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@ToString
@Accessors(chain = true, fluent = true)
@Table(
    name = "t_user",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_name", columnNames = "name")
)
@DynamicInsert
public class User extends AuditEntity {

    @Column(length = 32, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'UNKNOWN'")
    @Column(length = 10)
    private Gender gender;

    @Column(name = "birthday")
    private LocalDate birth;

    @Lob
    @Convert(converter = StrListConverter.class)
    @Column(columnDefinition = "text")
    private List<String> roles;

    private boolean online;
}