package ru.skillbox.authentication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.service.security.jwt.JwtService;
import ru.skillbox.commonlib.dto.auth.JwtRequest;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FeignClientController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/getclaims")
    public Map<String, Object> getJwtTokenClimesHandler(@RequestBody JwtRequest request) {
        return jwtService.getAllClaimsFromToken(request.getToken());
    }

    @GetMapping("/close")
    public void closeConnection(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setOnline(false);
        userRepository.save(user);
    }

}
