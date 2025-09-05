package br.mf.demospringsecurity.config;

import br.mf.demospringsecurity.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(TokenService tokenService){
        return new TokenAuthenticationFilter(tokenService);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TokenAuthenticationFilter tokenFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login",
                                "/auth/validate",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/products/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/requests/**").hasAnyRole("ADMIN", "CLIENT", "CONSULT")
                        .anyRequest().authenticated())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
