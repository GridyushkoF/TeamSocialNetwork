package ru.skillbox.authentication.service;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.captcha.CaptchaService;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.RoleType;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.security.JwtService;
import ru.skillbox.authentication.security.AppUserDetails;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationService  {

    private final AuthenticationManager authenticationManager;
    private final CaptchaService captchaService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){


        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
//        TODO Добавить реализацию рефреш токена. Данный метод должен возвращать два токена

//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        String jwt = jwtService.generateJwtToken(userDetails);

        return new AuthenticationResponse(jwt);

//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
//
//        List<String> roles = userDetails.getAuthorities()
//                .stream().map(GrantedAuthority::getAuthority).toList();
//
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
//
//        return AuthResponse.builder()
//                .id(userDetails.getId())
//                .username(userDetails.getUsername())
//                .refreshToken(refreshToken.getToken())
//                .roles(roles)
//                .token(jwtUtils.generateJwtToken(userDetails))
//                .email(userDetails.getEmail())
//                .build();

    }

//    private Map<String, Object> generateExtrsClaims(User users) {
//
//        Map<String, Object> extraClaims = new HashMap<>();
//
//        extraClaims.put("id" , users.getId());
//        extraClaims.put("name" , users.getSecondName());
//        extraClaims.put("role" , users.getRole().name());
//
//        return extraClaims;
//    }

    public void register(RegUserDto userDto) {
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .role(RoleType.USER)
                .password(passwordEncoder.encode(userDto.getPassword1()))
                .build();

        userRepository.save(user);
    }

//    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
//        String requestRefreshToken = refreshTokenRequest.getRefreshToken();
//
//        return refreshTokenService.findByRefreshToken(requestRefreshToken)
//                .map(refreshTokenService::checkRefreshToken)
//                .map(RefreshToken::getUserId)
//                .map(userId -> {
//                    User tokenOwner = userRepository.findById(userId)
//                            .orElseThrow(() -> new RefreshTokenException("Ошибка при получении токена по user id"));
//                    String token = jwtUtils.generateJwtTokenFromUsername(tokenOwner.getUsername());
//                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
//                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Токен не найден"));
//    }

    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();
//            refreshTokenService.deleteByUserId(userId);
        }
    }
}
