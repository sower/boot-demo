package me.boot.base.context;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * 基础上下文
 *
 * @date 2023/02/26
 */
@Data
public class BootContext {

    private String userId;

    private Map<String, Object> properties = new HashMap<>();

    public Object getProperty(String var) {
        return this.properties.get(var);
    }

    public void addProperty(String varKey, Object varValue) {
        this.properties.put(varKey, varValue);
    }

    public void removeProperty(String var) {
        this.properties.remove(var);
    }

    public void removeAllProperties() {
        this.properties.clear();
    }
}
