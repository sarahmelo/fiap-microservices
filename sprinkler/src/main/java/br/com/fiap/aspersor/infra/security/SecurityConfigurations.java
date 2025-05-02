package br.com.fiap.aspersor.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/sprinkler").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sprinkler/**").permitAll() // Permitir GET em endpoints específicos
                        .requestMatchers(HttpMethod.PUT, "/api/sprinkler").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sprinkler").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/sprinkler/**").permitAll() // Permitir DELETE para testes
                        .anyRequest().authenticated()
                )
                .build();
    }
}
