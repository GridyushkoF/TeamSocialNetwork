package ru.skillbox.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.gateway.security.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Autowired
    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "post_route", r -> r.path("/api/v1/post/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://POST-SERVICE")
                )
                .route(
                        "auth_route", r -> r.path("/api/v1/auth/**")
                                .uri("lb://AUTHENTICATION")
                )
                .route(
                        "user_route", r -> r.path("/api/v1/account/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://USER-SERVICE")
                )
                .route(
                        "user_route", r -> r.path("/api/v1/friends/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://USER-SERVICE")
                )
                .route(
                        "dialog_route", r -> r.path("/api/v1/dialogs/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://DIALOG-SERVICE")
                )
                .route(
                        "notification_route", r -> r.path("/api/v1/notifications/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://NOTIFICATION-SERVICE")
                )
                .build();
    }
}
