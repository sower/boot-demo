package me.boot.base.config;

import java.util.stream.Stream;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @date 2023/02/19
 */
//@EnableAsync
@Configuration
public class ThreadPoolConfig {

    private final int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
    private final int maxPoolSize = corePoolSize * 2;
    private final int queueCapacity = 50;
    private final int keepAliveSeconds = 30;

    @Bean
    public TaskExecutorBuilder taskExecutorBuilder(TaskExecutionProperties properties,
        ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
        ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        Stream<TaskExecutorCustomizer> customizerStream = taskExecutorCustomizers.orderedStream();
        builder = builder.queueCapacity(pool.getQueueCapacity());
        builder = builder.corePoolSize(pool.getCoreSize());
        builder = builder.maxPoolSize(pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder = builder.customizers(customizerStream::iterator);
        builder = builder.taskDecorator(taskDecorator.getIfUnique());
        return builder;
    }

    @Primary
    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }


    @Lazy
    @Bean(name = "singleTaskExecutor")
    public ThreadPoolTaskExecutor singleTaskExecutor(TaskExecutorBuilder builder) {
        return builder.corePoolSize(1).maxPoolSize(1).threadNamePrefix("singleTask-").build();
    }
}
