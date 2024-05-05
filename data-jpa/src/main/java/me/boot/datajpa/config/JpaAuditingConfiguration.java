package me.boot.datajpa.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JpaAuditingConfiguration
 *
 * @since 2022/09/26
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {

  @Bean
  public AuditorAware<String> auditorProvider() {

    /*
     if you are using spring security, you can get the currently logged username with following code segment.
     SecurityContextHolder.getContext().getAuthentication().getName()
    */
    return () -> Optional.of("system");
  }
}

