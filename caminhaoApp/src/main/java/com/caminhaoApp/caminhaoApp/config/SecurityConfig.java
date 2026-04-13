package com.caminhaoApp.caminhaoApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(f -> f.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/**", "/error").permitAll() 
                .requestMatchers("/login", "/usuarios/novo", "/usuarios/salvar", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .requestMatchers("/usuarios/listar").hasAnyRole("ADMIN", "EMPRESA", "CAMINHONEIRO")
                .requestMatchers("/rotas/salvar", "/rotas/excluir/**").hasAnyRole("ADMIN", "EMPRESA")
                .requestMatchers("/rotas/iniciar/**", "/fotos/**").hasRole("CAMINHONEIRO")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/usuarios/listar", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
