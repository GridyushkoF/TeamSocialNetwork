package ru.skillbox.authentication.service;



import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.exception.IncorrectPasswordException;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.Role;
import ru.skillbox.authentication.model.entity.User;
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

    private static final int BEAERER_TOKEN_INDEX = 7;


    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

            User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
            user.setOnline(true);
            userRepository.save(user);

            String jwt = jwtService.generateJwtToken(userDetails);
            log.info("Пользователь '" + authenticationRequest.getEmail() +
                    "' успешно прошел аутентификацию.");
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

        userRepository.save(user);
    }

    public void logout(String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(BEAERER_TOKEN_INDEX);
        String email = jwtService.getAllClaimsFromToken(jwtToken).getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setOnline(false);
        userRepository.save(user);

        log.info("Пользователь " + email + " вышел из системы.");
    }

    public void closeConnection(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setOnline(false);
        userRepository.save(user);


    }
}
