package com.example.simple_task_manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, TokenBasedRememberMeServices rememberMeServices) throws Exception {
        return httpSecurity.authorizeHttpRequests(request -> {
            request.requestMatchers("/login", "/registration", "/").permitAll()
                    .requestMatchers(HttpMethod.GET, "/images/**", "/css/**", "/avatars/**").permitAll()
                    .requestMatchers("/tasks", "/logout", "/settings", "/settings/**").authenticated()
                    .anyRequest().denyAll();
        }).formLogin(form ->
                        form.loginPage("/login")
                                .defaultSuccessUrl("/tasks", true))
                .rememberMe(rMe ->
                    rMe.rememberMeServices(rememberMeServices)
                            .key("AbcdefghiJklmNoPqRstUvXyz1234567890")
                            .tokenValiditySeconds(1209600) // 14 days
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices(UserDetailsService userDetailsService) {
        String key = "AbcdefghiJklmNoPqRstUvXyz1234567890";

        return new TokenBasedRememberMeServices(key, userDetailsService);
    }
}
