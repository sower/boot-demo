package me.boot.datajpa.criteria.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boot.datajpa.criteria.constant.QueryOperation;

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

    private QueryOperation operation;

    private Object value;

    /**
     * 值类型
     * <p>预留</p>
     */
    private Class<?> clazz;

}
