package me.boot.base.config;

import java.util.Map;
import java.util.UUID;
import me.boot.base.constant.Constants;
import me.boot.base.context.BootContext;
import me.boot.base.context.BootContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * MDC任务装饰器
 *
 * @since 2023/02/19
 */
@Component
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        BootContext context = BootContextHolder.getContext();
//    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return () -> {
            try {
                BootContextHolder.setContext(context);
                MDC.setContextMap(mdcMap);
//        RequestContextHolder.setRequestAttributes(attributes);
                String traceId = MDC.get(Constants.TRACE_ID);
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                    MDC.put(Constants.TRACE_ID, traceId);
                }
                runnable.run();
            } finally {
                MDC.clear();
                BootContextHolder.clearContext();
//        RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}
