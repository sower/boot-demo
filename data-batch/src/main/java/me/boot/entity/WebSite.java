package me.boot.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;

/**
 * website实体
 *
 * @date 2022/09/26
 */
@Data
@Entity
@Table(name = "website")
public class WebSite {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    private String url;

    @Lob
    private String description;
}
