package me.boot.dataaudit.config;

import org.javers.spring.auditable.AuthorProvider;
import org.springframework.context.annotation.Bean;

/**
 * JaversAuthorConfig
 *
 * @since 2023/07/16
 */
public class JaversAuthorConfig {

    private static class SimpleAuthorProvider implements AuthorProvider {
        @Override
        public String provide() {
            return "System";
        }
    }

    @Bean
    public AuthorProvider javersAuthor() {
        return new SimpleAuthorProvider();
//        return () -> "System";
    }
}
