package ru.skillbox.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.authentication.service.AuthenticationService;
import ru.skillbox.authentication.service.security.jwt.JwtService;
import ru.skillbox.commonlib.dto.auth.IsOnlineRequest;
import ru.skillbox.commonlib.dto.auth.JwtRequest;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Feign Сlient Controller", description = "Feign Client API")
public class FeignClientController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/getclaims")
    @Operation(summary = "Get Jwt token climes")
    public Map<String, Object> getJwtTokenClimesHandler(@RequestBody JwtRequest request) {
        return jwtService.getAllClaimsFromToken(request.getToken());
    }

    @PostMapping("/setIsOnline")
    @Operation(summary = "Set isOnline")
    public void setIsOnline(@RequestBody IsOnlineRequest request) {
        authenticationService.setIsOnline(request);
    }

}
