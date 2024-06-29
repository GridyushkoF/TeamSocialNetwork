package ru.skillbox.authentication.authentication;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.captcha.CaptchaService;
import ru.skillbox.authentication.dto.RegUserDto;
import ru.skillbox.authentication.entity.RoleType;
import ru.skillbox.authentication.entity.User;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.config.Jwt.JwtService;
import ru.skillbox.authentication.model.AppUserDetails;

import java.util.List;
import java.util.Set;

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

        String jwt = jwtService.generateJwtToken(userDetails);
        return new AuthenticationResponse(jwt);
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
                .roles(Set.of(RoleType.USER))
                .password(passwordEncoder.encode(userDto.getPassword1()))
                .build();

        userRepository.save(user);
    }

    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();
        }
    }
}
