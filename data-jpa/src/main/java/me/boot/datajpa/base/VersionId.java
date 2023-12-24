package me.boot.datajpa.base;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VersionId
 *
 * @since 2023/12/24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String version;

    private String id;

}
