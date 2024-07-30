package ru.skillbox.authentication.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.exception.IncorrectPasswordException;
import ru.skillbox.authentication.exception.RefreshTokenException;
import ru.skillbox.authentication.model.entity.RefreshToken;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
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
public class AuthenticationService  {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;


    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

            String jwt = jwtService.generateJwtToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
            user.setOnline(true);
            userRepository.save(user);


            log.info("Пользователь '" + authenticationRequest.getEmail() +
                    "' успешно прошел аутентификацию.");

            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken.getToken())
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
