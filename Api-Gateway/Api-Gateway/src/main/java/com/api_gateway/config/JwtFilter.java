package com.api_gateway.config;

import com.api_gateway.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements WebFilter {

    Logger logger= LoggerFactory.getLogger(JwtFilter.class);
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("JwtFilter is executing...");
        ServerHttpRequest request= exchange.getRequest();
        logger.info("Incoming request: {}", request.getURI().getPath());

        if (request.getURI().getPath().equals("/api/v1/token/accessToken") || request.getURI().getPath().equals("/api/v1/users/create")|| request.getURI().getPath().equals("/api/v1/users/login")) {
            return chain.filter(exchange)
                    .doOnSuccess(aVoid -> logger.info("Request successfully forwarded to downstream service: {}", request.getURI().getPath()))
                    .doOnError(error -> logger.error("Error in filter chain: ", error));
        }

        if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            logger.warn("Missing Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String authHeader= request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token= authHeader.substring(7);
        if(authHeader.startsWith("Bearer")){
            logger.info("Extracted Token: {}", token);
            try{
                if(!jwtService.validateToken(token)){
                    logger.warn("Token validation failed");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }catch (ExpiredJwtException e){
                logger.warn("Token expired");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().add("X-Token-Expired", "true");
                return exchange.getResponse().setComplete();
            }
        }
        String userId = jwtService.extractUserId(token);

        AbstractAuthenticationToken authentication = new AbstractAuthenticationToken(null) {
            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return userId;
            }
        };
        authentication.setAuthenticated(true);

        SecurityContext context = new SecurityContextImpl(authentication);
        logger.info("Authentication Completed!!!");
        logger.info("Forwarding request to downstream service: {}", request.getURI().getPath());
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)))
                .doOnSuccess(aVoid -> logger.info("Request successfully forwarded to downstream service: {}", request.getURI().getPath()))
                .doOnError(error -> logger.error("Error in filter chain: ", error));
    }
}
