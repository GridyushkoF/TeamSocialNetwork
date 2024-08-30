package ru.skillbox.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RouterValidator {

    public static final List<Pattern> openEndpoints = List.of(
            Pattern.compile("/api/v1/auth/(.*)"),
            Pattern.compile("/api/v1/streaming/ws"),
            Pattern.compile("([-/0-9a-zA-Z]*)/v3/api-docs.*"),
            Pattern.compile("([-/0-9a-zA-Z]*)/actuator/prometheus")
    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints
                    .stream()
                    .noneMatch(pattern -> pattern.matcher(request.getURI()
                            .getPath()).matches());
    private RouterValidator() {

    }

}
