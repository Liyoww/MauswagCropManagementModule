package com.cropmanager.cropsync;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Idagdag ito para masiguradong active ang security rules
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. I-disable ang CSRF (kailangan ito para sa POST requests sa H2 at API)
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Payagan ang access sa H2 Console path
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() 
                .anyRequest().permitAll()
            )
            
            // 3. SOBRANG IMPORTANTE: I-disable ang frameOptions
            // Ang H2 Console ay gumagamit ng <frame> tags. Bina-block ito ni Spring by default (Clickjacking protection).
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}