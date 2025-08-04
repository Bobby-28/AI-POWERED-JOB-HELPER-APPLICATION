package com.api_gateway.config;

import com.api_gateway.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    Logger logger= LoggerFactory.getLogger(SecurityConfig.class);

    JwtService jwtService;
    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        logger.info("Received request");
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange-> exchange
                        .pathMatchers("/api/v1/users/create", "/api/v1/users/login", "/api/v1/token/accessToken").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        logger.info("send to Jwt Filter!!!!!!!");
        return http.build();
    }
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService);
    }
}
