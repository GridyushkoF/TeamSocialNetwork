package ru.skillbox.authentication.authentication;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.dto.UserDto;
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
    private final  UserRepository userRepository;
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

//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        String jwt = jwtService.generateJwtToken(userDetails);

        return new AuthenticationResponse(jwt);


    }

    public void register(UserDto userDto) {
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
//            refreshTokenService.deleteByUserId(userId);
        }
    }
}
