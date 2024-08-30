package ru.skillbox.authentication.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.exception.IncorrectPasswordException;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.processor.AuditProcessor;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.service.security.jwt.JwtService;
import ru.skillbox.authentication.service.security.AppUserDetails;
import ru.skillbox.commonlib.dto.account.Role;
import ru.skillbox.commonlib.dto.auth.IsOnlineRequest;
import ru.skillbox.commonlib.event.audit.ActionType;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService  {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuditProcessor auditProcessor;

    private static final int BEARER_TOKEN_INDEX = 7;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

            User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new EntityNotFoundException(
                    "Пользователь с email: " + userDetails.getEmail() + " не найден"
            ));

            setIsOnline(new IsOnlineRequest(user.getId(), true));

            String jwt = jwtService.generateJwtToken(userDetails);
            log.info("Пользователь '{}' успешно прошел аутентификацию.", authenticationRequest.getEmail());
            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(jwt)
                    .build();
        } catch (RuntimeException e) {
            throw new IncorrectPasswordException("Для пользователя с e-mail " + authenticationRequest.getEmail() + " указан неверный пароль!");
        }
    }

    public void register(RegUserDto userDto) {
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(userDto.getPassword1()))
                .isOnline(false)
                .isBlocked(false)
                .isDeleted(false)
                .build();

        User newUser = userRepository.save(user);

        auditProcessor.process(newUser, ActionType.CREATE, newUser.getId());
    }

    public void logout(String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(BEARER_TOKEN_INDEX);
        String email = jwtService.getAllClaimsFromToken(jwtToken).getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email: " + email + " not found"));

        setIsOnline(new IsOnlineRequest(user.getId(), false));

        log.info("Пользователь {} вышел из системы.", email);
    }

    public void setIsOnline(IsOnlineRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + request.getUserId() + " not found"));
        user.setOnline(request.getIsOnline());
        user.setLastOnlineTime(ZonedDateTime.now());
        userRepository.save(user);
    }
}
