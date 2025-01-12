package com.example.demo.config;

import com.example.demo.config.CustomSessionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final CustomSessionFilter customSessionFilter;

    public SecurityConfig(CustomSessionFilter customSessionFilter) {
        this.customSessionFilter = customSessionFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true); // withCredentials 사용 허용
                    config.setAllowedOrigins(List.of("http://localhost:3000", "https://dreamcatcherrr.store")); // 허용 도메인
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    return config;
                }))

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS 요청 허용
                        .requestMatchers("/api/auth/**").permitAll() // 인증 요청 허용
                        .requestMatchers("/api/users/**").permitAll() // 사용자 요청 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )

                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 쿠키 기반 인증 시 변경 가능
                );

        // 커스텀 필터 추가
        http.addFilterBefore(customSessionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
