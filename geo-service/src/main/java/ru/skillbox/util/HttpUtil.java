package ru.skillbox.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
@Component
@RequiredArgsConstructor
public class HttpUtil {
    private final WebClient.Builder webClientBuilder;
    public String sendHttpAndGetTextResponse(String url){
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
