package ru.skillbox.dialogservice.service.security;

import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.skillbox.dialogservice.model.dto.JwtRequest;
import ru.skillbox.dialogservice.service.feign.DialogFeignClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {

    private final DialogFeignClient feignClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(authenticate(request));
        filterChain.doFilter(request, response);
    }

    private Authentication authenticate(HttpServletRequest request) {

        final String token = this.getToken(request);

        TokenAuthentication authentication =
                new TokenAuthentication(null, false, null);
        if (token != null) {
            try {
                Map<String, String> tokenPayload = feignClient.validateToken(new JwtRequest(token));
                authentication = new TokenAuthentication(
                        Arrays.stream(tokenPayload.get("authorities").split(","))
                                .map(authority -> (GrantedAuthority) () -> authority)
                                .toList(),
                        true,
                        tokenPayload.get("id")
                );
            } catch (FeignException e) {
                e.printStackTrace();
                log.info("ошибка валидации токена");
            }
        }
        return authentication;
    }


    public String getToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        return cookies != null ? Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("jwt"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null) : null;
    }

}
