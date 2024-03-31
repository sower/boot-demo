package me.boot.web.mvc.endpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

/**
 * BootEndpoint
 *
 * @since 2024/03/31
 **/
@Component
@Endpoint(id = "boot")
public class BootEndpoint {

    private final Map<String, Object> features = new ConcurrentHashMap<>();

    @ReadOperation
    public Map<String, Object> features() {
        return features;
    }

    @ReadOperation
    public Object feature(@Selector String name) {
        return features.get(name);
    }

    @WriteOperation
    public void configureObject(@Selector String name, Object feature) {
        features.put(name, feature);
    }

    @DeleteOperation
    public void deleteObject(@Selector String name) {
        features.remove(name);
    }

}
