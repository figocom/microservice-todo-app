package com.example.todoappmicroserviceuserapi.service;



import com.example.todoappmicroserviceuserapi.config.security.AuthUserDetails;
import com.example.todoappmicroserviceuserapi.config.security.JwtProvider;
import com.example.todoappmicroserviceuserapi.payload.LoginDto;
import com.example.todoappmicroserviceuserapi.repository.AuthUserRepository;
import com.example.todoappmicroserviceuserapi.response.TokenRefreshResponse;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    public AuthUserDetailsService(AuthUserRepository authUserRepository, @Lazy AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authUserRepository = authUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthUserDetails(authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    public TokenRefreshResponse generateToken(@NonNull LoginDto dto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (!authenticate.isAuthenticated())
            throw new BadCredentialsException("Bad credentials");
        String accessToken = jwtProvider.generateToken(Map.of(), loadUserByUsername(dto.getUsername()).authUser(), false);
        String refreshToken = jwtProvider.generateToken(loadUserByUsername(dto.getUsername()).authUser());
        return TokenRefreshResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
