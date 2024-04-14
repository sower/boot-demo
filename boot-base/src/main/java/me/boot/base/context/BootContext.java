package me.boot.base.context;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Delegate;

/**
 * 基础上下文
 *
 * @date 2023/02/26
 */
@Data
public class BootContext {

    private String userId;

    @Delegate
    private Map<String, Object> properties = new HashMap<>();

}
