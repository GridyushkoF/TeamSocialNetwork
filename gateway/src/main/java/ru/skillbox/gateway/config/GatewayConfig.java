package ru.skillbox.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.gateway.security.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;
    @Value("${app.userMicroservicePath}")
    private String pathToUserMicroservice;

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
                        "post_route", r -> r.path("/api/v1/tag/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://POST-SERVICE")
                )
                .route(
                        "auth_route", r -> r.path("/api/v1/auth/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://AUTHENTICATION")
                )
                .route(
                        "account_route", r -> r.path("/api/v1/account/**")
                                .filters(f -> f.filter(filter))
                                .uri(pathToUserMicroservice)
                )
                .route(
                        "friends_route", r -> r.path("/api/v1/friends/**")
                                .filters(f -> f.filter(filter))
                                .uri(pathToUserMicroservice)
                )
                .route(
                        "storage_route", r -> r.path("/api/v1/storage/**")
                                .filters(f -> f.filter(filter))
                                .uri(pathToUserMicroservice)
                )
                .route(
                        "dialog_route", r -> r.path("/api/v1/dialogs/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://DIALOG-SERVICE")
                )
                .route(
                        "dialog_route", r -> r.path("/api/v1/streaming/ws/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://DIALOG-SERVICE")
                )
                .route(
                        "notification_route", r -> r.path("/api/v1/notifications/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://NOTIFICATION-SERVICE")
                )
                .route(
                        "geo_route", r -> r.path("/api/v1/geo/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://GEO-SERVICE")
                )
                .route(
                        "admin_route", r -> r.path("/api/v1/admin-console/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://ADMIN-CONSOLE")
                )
                .build();
    }
}
