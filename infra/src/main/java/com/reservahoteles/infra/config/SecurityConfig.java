package com.reservahoteles.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration("infraSecurityConfig")
@EnableWebSecurity
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder fuerte y adaptativo.
     * Lo inyectas en tus servicios con @Autowired o constructor.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}