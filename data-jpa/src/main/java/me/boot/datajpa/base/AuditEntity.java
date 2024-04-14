package me.boot.datajpa.base;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 审计基类
 *
 * @date 2022/09/26
 */
@ToString
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "me.boot.datajpa.generator.AutoUUIDGenerator")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    private LocalDateTime createTime;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 30)
    private String creator;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "timestamp")
    private LocalDateTime updateTime;

    @LastModifiedBy
    @Column(nullable = false, length = 30)
    private String updater;
}
