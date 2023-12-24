package me.boot.datajpa.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
public class SoftDeleteEntity extends AuditEntity {

    @Column(name = "is_deleted")
    private boolean deleted;

}
