package me.boot.datajpa.criteria.constant;

public enum QueryOperation {
    // 相等
    EQUAL,

    // 不等于
    NOT_EQUAL,

    // 模糊查询
    LIKE,

    // 左模糊查询
    LEFT_LIKE,

    // 右模糊查询
    RIGHT_LIKE,

    // 包含
    IN,

    // 不包含
    NOT_IN,

    // 大于等于
    GREATER_THAN,

    // 小于等于
    LESS_THAN,

    // 小于
    LESS_THAN_NQ,

    // 之间
    BETWEEN,

    // 不为空
    NOT_NULL,

    // 为空
    IS_NULL
}

