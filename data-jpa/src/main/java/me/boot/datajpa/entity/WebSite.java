package me.boot.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.boot.datajpa.base.SoftDeleteEntity;
import me.boot.datajpa.base.VersionId;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * website实体
 *
 * @since 2022/09/26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true, fluent = true)
@Entity
@IdClass(VersionId.class)
@Table(name = "t_website")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "update t_website set is_deleted = true where version = ? and id = ?")
public class WebSite extends SoftDeleteEntity {

    @Id
    @Column(length = 10, nullable = false)
    private String version;

    @Column(length = 50)
    private String name;

    @Column(length = 500, nullable = false)
    private String url;

    private String description;
}
