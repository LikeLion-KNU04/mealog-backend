package knulions.mealog.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;


import knulions.mealog.auth.itself.filter.CustomJsonUserPasswordAuthenticationFilter;
import knulions.mealog.auth.itself.handler.LoginFailureHandler;
import knulions.mealog.auth.itself.handler.LoginSuccessHandler;
import knulions.mealog.auth.itself.service.LoginService;
import knulions.mealog.auth.jwt.filter.JwtAuthorizationFilter;
import knulions.mealog.auth.jwt.filter.JwtExceptionHandlerFilter;
import knulions.mealog.auth.oauth2.handler.CustomOauth2SuccessHandler;
import knulions.mealog.auth.oauth2.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
    private final CustomOauth2UserService customOauth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomOauth2SuccessHandler customOauth2SuccessHandler;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .cors(corsCustomizer ->corsCustomizer.configurationSource(corsConfigurationSource()))
                        .csrf(AbstractHttpConfigurer::disable)
                        .formLogin(AbstractHttpConfigurer::disable)
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/members/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/jwt/reissue")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/reissue")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/googleLogin")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/member/signup")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/email/**")).permitAll()
                                //.requestMatchers(new AntPathRequestMatcher("/program/{programId}/review/view/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/ws")).permitAll()

                                .anyRequest().authenticated()
                        )
                        .oauth2Login((oauth2) -> oauth2
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService))
                                .successHandler(customOauth2SuccessHandler))
                        .addFilterAfter(customJsonUserPasswordAuthenticationFilter(), LogoutFilter.class)
                        .addFilterBefore(jwtAuthorizationFilter, CustomJsonUserPasswordAuthenticationFilter.class)
                        .addFilterBefore(jwtExceptionHandlerFilter, JwtAuthorizationFilter.class)
                        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider(passwordEncoder);

        provider.setUserDetailsService(loginService);

        return new ProviderManager(provider);
    }
    @Bean
    public CustomJsonUserPasswordAuthenticationFilter customJsonUserPasswordAuthenticationFilter() {
        CustomJsonUserPasswordAuthenticationFilter customJsonUserPasswordAuthenticationFilter
                = new CustomJsonUserPasswordAuthenticationFilter(objectMapper);

        customJsonUserPasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUserPasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        customJsonUserPasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);

        return customJsonUserPasswordAuthenticationFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*"));

        config.setMaxAge(3600L); //1시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
