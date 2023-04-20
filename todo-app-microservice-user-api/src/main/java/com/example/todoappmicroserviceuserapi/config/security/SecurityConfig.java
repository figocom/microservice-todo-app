package com.example.todoappmicroserviceuserapi.config.security;


import com.example.todoappmicroserviceuserapi.service.AuthUserDetailsService;
import com.example.todoappmicroserviceuserapi.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true

)
@ComponentScan("com.example.todoappmicroserviceuserapi")
public class SecurityConfig implements WebSecurityConfigurer<WebSecurity> {
    private final Environment env;
    private final UserService userService;
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(Environment env, UserService userService, AuthUserDetailsService authUserDetailsService,
                          JwtFilter jwtFilter) {
        this.env = env;
        this.userService = userService;
        this.authUserDetailsService = authUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    public static final String[] WHITE_LIST = {
            "/v1/auth/login",
            "/v1/auth/register",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/user-docs"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpReq -> httpReq
                        .requestMatchers(WHITE_LIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(authUserDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, AuthUserDetailsService authUserDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


    @Override
    public void init(WebSecurity builder) throws Exception {
    }

    @Override
    public void configure(WebSecurity builder) throws Exception {

    }
}
