package ru.skillbox.authentication.service;


import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.authentication.exception.AlreadyExistsException;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.exception.IncorrectPasswordException;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.security.AppUserDetails;
import ru.skillbox.authentication.exception.RefreshTokenException;
import ru.skillbox.authentication.model.entity.RefreshToken;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.processor.AuditProcessor;
import ru.skillbox.authentication.repository.nosql.EmailChangeRequestRepository;
import ru.skillbox.authentication.repository.sql.UserRepository;
import ru.skillbox.authentication.service.security.jwt.JwtService;
import ru.skillbox.commonlib.dto.account.Role;
import ru.skillbox.commonlib.dto.auth.IsOnlineRequest;
import ru.skillbox.commonlib.event.audit.ActionType;

import java.time.ZonedDateTime;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.Role;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.model.web.RefreshTokenRequest;
import ru.skillbox.authentication.model.web.RefreshTokenResponse;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.service.security.Jwt.JwtService;
import ru.skillbox.authentication.service.security.AppUserDetails;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuditProcessor auditProcessor;
    private final EmailChangeRequestRepository emailChangeRequestRepository;
    private final RefreshTokenService refreshTokenService;
    private static final int BEARER_TOKEN_INDEX = 7;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {

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
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
            user.setOnline(true);
            userRepository.save(user);


            log.info("Пользователь '{}' успешно прошел аутентификацию.", authenticationRequest.getEmail());
            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken.getToken())
                    .build();
        } catch (RuntimeException e) {
            throw new IncorrectPasswordException("Для пользователя с e-mail " + authenticationRequest.getEmail() + " указан неверный пароль!");
        }
    }

    public void register(RegUserDto userDto) {
        User user = regUserDtoToUser(userDto);
        throwExceptionIfThereIsChangeEmailRequestWithThisEmail(user);
        throwExceptionIfEmailExists(user);
        log.info(emailChangeRequestRepository.findAll().toString());
        User newUser = userRepository.save(user);
        auditProcessor.process(newUser, ActionType.CREATE, newUser.getId());
    }

    public void throwExceptionIfThereIsChangeEmailRequestWithThisEmail(User user) {
        if (emailChangeRequestRepository.findByNewEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("this email is busy, because somebody going to change email to this, try again later or connect with email owner");
        }
    }

    public void throwExceptionIfEmailExists(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("this email is registered now!");
        }
    }

    private User regUserDtoToUser(RegUserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(userDto.getPassword1()))
                .isOnline(false)
                .isBlocked(false)
                .isDeleted(false)
                .build();
    }
    @Transactional
    public void logout(String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(BEARER_TOKEN_INDEX);
        try {
            String email = jwtService.getAllClaimsFromToken(jwtToken).getSubject();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new EntityNotFoundException("User with email: " + email + " not found"));

            setIsOnline(new IsOnlineRequest(user.getId(), false));

            log.info("Пользователь {} вышел из системы.", email);
        } catch (MalformedJwtException e) {
            throw new EntityNotFoundException("token " + authorizationHeader + "not valid");
        }
    }

    public void setIsOnline(IsOnlineRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + request.getUserId() + " not found"));
        user.setOnline(request.getIsOnline());
        user.setLastOnlineTime(ZonedDateTime.now());
        userRepository.save(user);
    }


    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Неудачная попытка получить токен для userId" + userId));
                    AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder
                            .getContext().getAuthentication().getPrincipal();
                    String token = jwtService.generateJwtToken(userDetails);
                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh токен не найден"));
    }

    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof  AppUserDetails userDetails) {
            Long userId = userDetails.getId();

            refreshTokenService.deleteByUserId(userId);
        }
    }


}
