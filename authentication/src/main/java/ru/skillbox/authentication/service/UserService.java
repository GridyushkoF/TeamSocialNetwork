package ru.skillbox.authentication.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.DTO.UserDTO;
import ru.skillbox.authentication.Entity.Role;
import ru.skillbox.authentication.Entity.User;
import ru.skillbox.authentication.Repository.UserRepository;
import ru.skillbox.authentication.config.Jwt.JwtService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();
        if (user == null)
            throw new UsernameNotFoundException("user not found");
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public void createUser(UserDTO user){
        User user2 = new User();
        user2.setPassword(user.getPassword1());
        user2.setEmail(user.getEmail());
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setPassword(passwordEncoder.encode(user.getPassword1()));

        Role role = Role.valueOf(jwtService.extractAllClaims(user.getToken()).get("role").toString());

        user2.setRole(role);

        userRepository.save(user2);
    }
}
