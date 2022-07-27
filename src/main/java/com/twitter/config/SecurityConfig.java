package com.twitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
public class SecurityConfig {



    private static final String[] WHITE_LIST_URLS = {
            "/register", "/users", "/verifyRegistration", "/resendVerifyToken",
            "/resetPassword", "/savePassword", "/changePassword", "/tweet","/tweets",
            "tweet/{tweetId}", "follow/user/{userId}", "/login", "/user/{userId}"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(WHITE_LIST_URLS)
                .permitAll().anyRequest().
                authenticated()
                .and()
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }



    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
