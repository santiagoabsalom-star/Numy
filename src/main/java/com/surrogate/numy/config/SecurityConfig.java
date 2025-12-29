package com.surrogate.numy.config;


import com.surrogate.numy.utils.DaoAuthenticationProviderWithId;
import com.surrogate.numy.utils.UserDetailsServiceWithId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;
    private final LoggingFilter loggingFilter;
    private final UserDetailsServiceWithId userDetailsService;
//    private final CustomOAuth2LoginSuccessHandler loginSuccessHandler;

    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/login").permitAll()

                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/admin/register").permitAll()
                        .requestMatchers("/actuator").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api/auth/me").hasAnyAuthority("ROLE_ADMIN", "ROLE_ADMINISTRADOR","ROLE_ADOPTANTE","ROLE_USUARIO","ROLE_SYSTEM","ROLE_USER")
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/usuario/getUsuarioByIdInstitucion").hasAnyAuthority("ROLE_ADMIN", "ROLE_ADMINISTRADOR","ROLE_ADOPTANTE","ROLE_USUARIO","ROLE_SYSTEM","ROLE_USER")
                        .requestMatchers("/api/auth/accounts").hasAnyAuthority("ROLE_ADMIN", "ROLE_ADMINISTRADOR", "ROLE_SYSTEM","ROLE_USUARIO","ROLE_ADOPTANTE","ROLE_USER")
                        .requestMatchers("/api/usuario/getUsuarioById").permitAll()
                        .requestMatchers("/chat/**").permitAll()

                        .requestMatchers("/api/usuario/eliminar").hasAnyAuthority("ROLE_SYSTEM")
                        .requestMatchers("/api/usuario/getUsuarios").hasAnyAuthority("ROLE_SYSTEM")
                        .anyRequest().authenticated())
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorization"))
//                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*"))
//                        .successHandler(loginSuccessHandler)
//                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .xssProtection(HeadersConfigurer.XXssConfig::disable)
                        .contentSecurityPolicy(csp -> csp.policyDirectives((
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                                        "style-src 'self' 'unsafe-inline'; " +
                                        "font-src 'self' data:;"
                        ))))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();


    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://192.168.0.45:5173",
                "http://localhost:5174",
                "http:/192,168.0.45",
                "null",
                "http://172.18.0.1:5173",
                "http://172.18.0.1:3000",
                "http://172.20.10.8:5173"

        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-CSRF-TOKEN",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Allow-Origin",
                "Access-Control-Request-Headers",
                "X-Requested-With"

        ));
        configuration.setExposedHeaders(List.of("Authorization", "Id-Usuario", "Nombre-Usuario"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProviderWithId authProvider = new DaoAuthenticationProviderWithId();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {


        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public String success() {
        return "success";
    }

    @Bean
    public String error() {
        return "error";
    }

}
