package me.boot.dataaudit.base;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 实体基类
 *
 * @date 2022/09/26
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @CreatedDate
    @Column(name = "create_time", updatable = false, columnDefinition = "timestamp")
    private LocalDateTime createTime;

    @CreatedBy
    @Column(name = "creator", updatable = false, length = 30)
    private String creator;

    @LastModifiedDate
    @Column(name = "update_time", columnDefinition = "timestamp")
    private LocalDateTime updateTime;

    @LastModifiedBy
    @Column(name = "updater", length = 30)
    private String updater;
}
