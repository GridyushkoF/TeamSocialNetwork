package ru.skillbox.authentication.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.skillbox.authentication.config.Jwt.JwtAuthEntryPoint;
import ru.skillbox.authentication.config.Jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {

// TODO Возможно данный класс и не нужен.
//  Так как в нем только создание Бинов, что можно перенести в BeanInjector (эти 2 бина я пока перенес)



    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrfConfig -> csrfConfig.disable())
//                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .authorizeHttpRequests( authConfig -> {
//                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
//              //      authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
//                    authConfig.requestMatchers("/error").permitAll();
//
//                 //   authConfig.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
//                 //   authConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());
//
//                    authConfig.anyRequest().denyAll();
//
//                });
//
//
//        return http.build();
//
//    }

//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI().addSecurityItem(new SecurityRequirement()
//                        .addList("bearerAuth")).components(
//                        new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
//                                                .type(SecurityScheme.Type.HTTP)
//                                                .scheme("bearer")
//                                                .bearerFormat("JWT")))
//                .info(new Info()
//                        .title("Auth Controllers")
//                        .description("Социальная сеть")
//                        .version("1.0"));
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .authorizeHttpRequests((auth) ->
//                        auth.requestMatchers("/api/v1/auth/**")
//                                .permitAll()
////                                .requestMatchers("/api/v1/app/**")
////                                .permitAll()
//                                .requestMatchers("/swagger-ui/**")
//                                .permitAll()
//                                .requestMatchers("/v3/api-docs/**")
//                                .permitAll()
//                                .anyRequest().authenticated())
//                .exceptionHandling(conf -> conf.authenticationEntryPoint(jwtAuthEntryPoint))
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(httpSecuritySessionManagementConfigurer ->
//                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(new DaoAuthenticationProvider())
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
}

