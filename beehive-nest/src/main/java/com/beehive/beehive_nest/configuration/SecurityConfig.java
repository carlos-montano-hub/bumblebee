package com.beehive.beehive_nest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiAuthenticationFilter apiAuthFilter = new ApiAuthenticationFilter();
        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter();
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for testing (not recommended for
                                                       // production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/api/measure/audio/**")
                        .permitAll() // Allow these endpoints without authentication
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .addFilterBefore(apiAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter to handle token
                                                                                   // validation
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:4200"); // Allow Angular app origin
                    config.addAllowedOrigin("http://192.168.100.80:4200");
                    config.addAllowedOrigin("http://localhost");
                    config.addAllowedOrigin("capacitor://localhost");
                    config.addAllowedMethod("*"); // Allow all HTTP methods
                    config.addAllowedHeader("*"); // Allow all headers
                    config.setAllowCredentials(true); // Allow credentials (cookies, headers, etc.)
                    return config;
                }));

        return http.build(); // Return the SecurityFilterChain
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
