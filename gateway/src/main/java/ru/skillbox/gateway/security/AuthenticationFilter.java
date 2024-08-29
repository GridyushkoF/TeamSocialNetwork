package ru.skillbox.gateway.security;

import io.jsonwebtoken.Claims;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    protected final JwtUtil jwtUtil;
    protected final Counter invalidAuthCounter;

    @Autowired
    public AuthenticationFilter(JwtUtil jwtUtil, MeterRegistry meterRegistry) {
        this.jwtUtil = jwtUtil;
        this.invalidAuthCounter = Counter
                .builder("invalid.auth.counter")
                .tag("auth_status","invalid")
                .description("count amount of invalid auth by user by every reason")
                .register(meterRegistry);
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = null;
        if (!isAuthMissing(request)) {
            token = this.getAuthHeader(request);
        }

        if (RouterValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            if (jwtUtil.isInvalid(token)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }
        if (!isAuthMissing(request)) {
            this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }

    protected Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        invalidAuthCounter.increment();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    protected String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    protected boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    protected void populateRequestWithHeaders(ServerWebExchange exchange, String tokenString) {
        String jwtToken = tokenString.substring(7);
        Claims tokenClaims = jwtUtil.getAllClaimsFromToken(jwtToken);
        String userId = tokenClaims.get("id").toString();
        String authorities = tokenClaims.get("authorities").toString();
        exchange.getRequest().mutate()
                .header("id", userId)
                .header("authorities",authorities)
                .build();
    }
}
