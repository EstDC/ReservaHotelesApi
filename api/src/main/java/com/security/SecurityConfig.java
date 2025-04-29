package com.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration("apiSecurityConfig")
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter() {
        return new HeaderAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) Sin CSRF (API REST JSON con tokens/cabeceras)
                .csrf(csrf -> csrf.disable())

                // 2) Stateless (sin sesión HTTP)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(
                                org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                )

                // 3) Inyecta tu filtro de cabeceras antes del UsernamePasswordAuthenticationFilter
                .addFilterBefore(
                        headerAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                // 4) Reglas de acceso
                .authorizeHttpRequests(authz -> authz

                        // → Permite sin actor autenticado:
                        //    • Login/recuperar
                        //    • Auto‑registro de clientes
                        .requestMatchers(
                                "/",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/configuration/**"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/recuperar",
                                "/api/auth/recuperar/confirmar",
                                "/api/clientes"
                        ).permitAll()

                        // → El resto exige un actor válido (injected por HeaderAuthenticationFilter)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}