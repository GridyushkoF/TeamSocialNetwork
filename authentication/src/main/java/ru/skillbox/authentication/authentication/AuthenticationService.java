package ru.skillbox.authentication.authentication;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.Entity.User;
import ru.skillbox.authentication.Repository.UserRepository;
import ru.skillbox.authentication.config.Jwt.JwtService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()
        );


        authenticationManager.authenticate(authToken);


        User user = userRepository.findByEmail(authenticationRequest.getEmail()).get();

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(jwt);

    }

    private Map<String, Object> generateExtraClaims(User user) {

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("name" , user.getLastName());
        extraClaims.put("role" , user.getRole().name());

        return extraClaims;
    }
}
