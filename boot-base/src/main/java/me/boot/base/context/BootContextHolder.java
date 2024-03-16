package me.boot.base.context;

/**
 * 上下文句柄
 *
 * @date 2023/02/26
 */
public class BootContextHolder {

    private static final ThreadLocal<BootContext> contextHolder = new InheritableThreadLocal<>();

    public static void setContext(BootContext bootContext) {
        contextHolder.set(bootContext);
    }

    public static BootContext getContext() {
        BootContext bootContext = contextHolder.get();
        if (bootContext == null) {
            bootContext = new BootContext();
            setContext(bootContext);
        }

        return bootContext;
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static String getUserId() {
        return getContext().getUserId();
    }
}
