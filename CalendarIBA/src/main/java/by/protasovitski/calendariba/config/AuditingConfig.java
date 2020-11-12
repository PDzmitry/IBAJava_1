package by.protasovitski.calendariba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditingConfig {
    @Bean
    public AuditorAware<String> createAuditorProvider(){
        return new SecurityAuditor();
    }
    public static class SecurityAuditor implements AuditorAware<String>{

        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            return Optional.of(username);
        }
    }
}
