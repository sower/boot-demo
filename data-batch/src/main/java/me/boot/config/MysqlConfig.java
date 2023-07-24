package me.boot.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * mysql 用于jpa
 *
 * @date 2023/03/05
 **/
@Configuration
//@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "mysqlEntityManagerFactory",
    transactionManagerRef = "mysqlTransactionManager", //配置事物管理器
    basePackages = {"me.boot.repository"}  //dao所在位置
)
@AllArgsConstructor
public class MysqlConfig {

    private JpaProperties jpaProperties;
    private HibernateProperties hibernateProperties;

    @Bean
    @ConfigurationProperties(prefix = "datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {
        return builder.dataSource(mysqlDataSource())
            .properties(
                hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
                    new HibernateSettings()))
            // 设置映射的实体类位置
            .packages("me.boot.entity")
            //持久化单元名称
            .persistenceUnit("mysql")
            .build();
    }

    @Bean
    public PlatformTransactionManager mysqlTransactionManager(
        EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
