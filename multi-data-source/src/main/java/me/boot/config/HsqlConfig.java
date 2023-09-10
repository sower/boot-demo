package me.boot.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 嵌入式(内存级别)数据库Hsql 用于创建初始化batch相关表
 *
 * @date 2023/03/05
 **/
@Configuration
public class HsqlConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.hsql")
    @Primary
    public DataSource hsqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager hsqlTransactionManager(DataSource hsqlDataSource) {
        return new DataSourceTransactionManager(hsqlDataSource);
    }

}
