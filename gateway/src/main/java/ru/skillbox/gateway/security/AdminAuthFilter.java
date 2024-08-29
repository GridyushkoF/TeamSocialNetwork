package ru.skillbox.gateway.security;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@RefreshScope
@Component
public class AdminAuthFilter extends AuthenticationFilter{
    public AdminAuthFilter(JwtUtil jwtUtil, MeterRegistry meterRegistry) {
        super(jwtUtil, meterRegistry);
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
            final String token = this.getAuthHeader(request);
            this.populateRequestWithHeaders(exchange, token);
            List<String> authorities = exchange.getRequest().getHeaders().get("authorities");
        assert authorities != null;
        for (String authority : authorities) {
            if(authority.toUpperCase().contains("ADMIN")) {
                return chain.filter(exchange);
            }
        }
        return onError(exchange, HttpStatus.UNAUTHORIZED);
    }
}
