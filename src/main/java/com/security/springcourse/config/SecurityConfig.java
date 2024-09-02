package com.security.springcourse.config;

import com.security.springcourse.services.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean // Возвращаем кастомный PersonDetailsService, который напишем далее
    public UserDetailsService userDetailsService() {
        return new PersonDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean // Ставим степень кодировки, с которой кодировали пароль в базе
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(5);
        return NoOpPasswordEncoder.getInstance();
    }

}
