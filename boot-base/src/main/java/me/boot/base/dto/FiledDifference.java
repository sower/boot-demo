package me.boot.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FiledDifference
 *
 * @since 2024/01/07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiledDifference {

    private String name;

    private Object oldValue;

    private Object newValue;

}
