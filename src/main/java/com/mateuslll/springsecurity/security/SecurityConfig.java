package com.mateuslll.springsecurity.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5500"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws SecurityConfigurationException {
        try {
            http.cors(Customizer.withDefaults())
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(HttpMethod.GET, "/api/message").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/admin").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/regular").hasRole("USER")
                            .anyRequest().authenticated())
                    .formLogin(Customizer.withDefaults())
                    .httpBasic(Customizer.withDefaults()
                    );

            return http.build();
        } catch (Exception ex) {
            throw new SecurityConfigurationException("Failed to configure security filter chain", ex);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // por enquanto vamos usar em memória, depois vamos usar o banco de dados
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var admin = User.withUsername("admin")
                .passwordEncoder(passwordEncoder::encode)
                .password("123")
                .roles("ADMIN")
                .build();

        var regular = User.withUsername("regular")
                .passwordEncoder(passwordEncoder::encode)
                .password("123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, regular);
    }

    //fluxo criar conta => usuario -> 123 -> bcrypt -> $2a$10$...

    // login => usuario -> 123 -> bcrypt -> $2a$10$... -> comparar com o hash armazenado == bcrypt.matches("123", "$2a$10$...")
}
