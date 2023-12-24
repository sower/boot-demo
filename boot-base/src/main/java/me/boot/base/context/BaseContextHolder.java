package me.boot.base.context;

/**
 * 上下文句柄
 *
 * @date 2023/02/26
 */
public class BaseContextHolder {

    private static final ThreadLocal<BaseContext> contextHolder = new InheritableThreadLocal<>();

    public static void setContext(BaseContext baseContext) {
        contextHolder.set(baseContext);
    }

    public static BaseContext getContext() {
        BaseContext baseContext = contextHolder.get();
        if (baseContext == null) {
            baseContext = new BaseContext();
            setContext(baseContext);
        }

        return baseContext;
    }

    public void clearContext() {
        contextHolder.remove();
    }
}
