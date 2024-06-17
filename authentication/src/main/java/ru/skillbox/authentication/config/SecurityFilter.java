package ru.skillbox.authentication.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityFilter {
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityFilter(AuthenticationProvider authenticationProvider){
        this.authenticationProvider = authenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests( authConfig -> {
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
              //      authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                 //   authConfig.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
                 //   authConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());

                    authConfig.anyRequest().denyAll();

                });


        return http.build();

    }
}

