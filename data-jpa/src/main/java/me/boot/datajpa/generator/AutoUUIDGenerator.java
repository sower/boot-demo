package me.boot.datajpa.generator;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.UUID;
import javax.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 自动id生成器
 *
 * @since 2023/11/18
 **/
@Slf4j
public class AutoUUIDGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object entity) {
        // 如果主键有值，则用实体自带的主键值
        try {
            Field idField = FieldUtils.getFieldsListWithAnnotation(entity.getClass(),
                Id.class).get(0);
            Object id = FieldUtils.readField(idField, entity, true);
            if (id instanceof String) {
                String idStringValue = (String) id;
                if (StringUtils.isNotBlank(idStringValue)) {
                    return idStringValue;
                }
            }
        } catch (Exception e) {
            log.error("Failed to get {} id", entity.getClass(), e);
        }
        return UUID.randomUUID().toString().replace("-", "");
    }
}
