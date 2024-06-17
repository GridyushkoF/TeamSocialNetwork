package ru.skillbox.authentication.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.Repository.UserRepository;
import ru.skillbox.authentication.service.UserService;

@Component
public class SecurityBeanInjector {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public SecurityBeanInjector(UserRepository userRepository , UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();//providerManager implements AuthenticationManager
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

        /*@Bean
        public UserDetailsService detailsService(){
            return email -> {
                    userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User is not found"));
            };
        }
         */

}
