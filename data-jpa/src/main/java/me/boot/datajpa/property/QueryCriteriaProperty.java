package me.boot.datajpa.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boot.base.constant.Operation;

/**
 * QueryCriteriaProperty
 *
 * @since 2023/12/24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCriteriaProperty {

    private String name;

    private Operation operation;

    private Object value;

    private Class<?> clazz;

}
