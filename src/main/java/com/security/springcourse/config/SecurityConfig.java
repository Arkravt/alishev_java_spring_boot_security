package com.security.springcourse.config;

import com.security.springcourse.repositories.PeopleRepository;
import com.security.springcourse.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Отключаем защиту от межсайтовой подделки запросов
                .authorizeHttpRequests(authrize -> authrize
                        .requestMatchers("/auth/login", "/error").permitAll() // только эти запросы разрешаем для не авторизованных пользователей.
                        .anyRequest().authenticated() // для всех остальных запросов пользователь должен быть аутентифицирован.
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login") // указываем адрес нашей странички логина.
                        .loginProcessingUrl("/process_login") // указываем куда передаем данные с формы.
                        .defaultSuccessUrl("/hello", true) // указываем страницу при успешной аутентификации.
                        .failureUrl("/auth/login?error")); // указываем страницу при не верной аутентификации.

        return http.build();

    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(5);
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean // Определяем AuthenticationProvider по умолчанию если нету своего кастомного
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(personDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

}
