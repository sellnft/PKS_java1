package org.example.spring_kr2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // Настраиваем правила доступа
                .authorizeHttpRequests(auth -> auth
                        // Эндпоинты, доступные БЕЗ токена (регистрация и логин)
                        .requestMatchers("/api/users/register", "/api/announcements/create", "/api/announcements/get/*", "/api/announcements/all", "/api/announcements/*/setEmployee/*", "/api/announcements/setDoneStatus/*").permitAll()
                        // Всё остальное — только с токеном
                        .anyRequest().authenticated()
                )

                // Говорим Spring Security не создавать сессии (для JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
