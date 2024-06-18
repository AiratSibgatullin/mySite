package com.example.mySite.config;

import com.example.mySite.servises.PersonDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PersonDetailService personDetailService;

    public SecurityConfig(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .formLogin(formLogin -> formLogin.loginPage("/auth")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/info", true)
                        .failureUrl("/auth?error"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/update/**", "/info/**", "/delete/**", "/sucreg", "/sucupdate").authenticated()
                        .requestMatchers("/view/**").hasRole("ADMIN")
                        .requestMatchers("/", "/auth/**", "/reg/**").permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
