package com.example.todoappmicroserviceuserapi.service;



import com.example.todoappmicroserviceuserapi.mapper.UserMapper;
import com.example.todoappmicroserviceuserapi.model.AuthUser;
import com.example.todoappmicroserviceuserapi.payload.LoginDto;
import com.example.todoappmicroserviceuserapi.payload.RegisterDto;
import com.example.todoappmicroserviceuserapi.repository.AuthUserRepository;
import com.example.todoappmicroserviceuserapi.response.DataDTO;
import com.example.todoappmicroserviceuserapi.response.ErrorDTO;
import com.example.todoappmicroserviceuserapi.response.TokenRefreshResponse;
import com.example.todoappmicroserviceuserapi.response.WebResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final UserMapper userMapper;
    private final AuthUserDetailsService authUserDetailsService;


    public WebResponse<?> login(LoginDto loginDto) {
        try {
            TokenRefreshResponse response = authUserDetailsService.generateToken(loginDto);
            return new WebResponse<>(new DataDTO<>(response));
        } catch (BadCredentialsException badCredentialsException) {
            return new WebResponse<>(new ErrorDTO(badCredentialsException, 400));
        }
    }

    public WebResponse<?> register(RegisterDto registerDto) {
        if (Objects.isNull(registerDto.getConfirmPassword()) || !registerDto.getConfirmPassword().equals(registerDto.getPassword()))
            return new WebResponse<>(new ErrorDTO("Confirm password is  not equals to password", 400));
        AuthUser entity = userMapper.fromCreateDTO(registerDto);
        if (Objects.nonNull(registerDto.getUsername())) {
            Optional<AuthUser> byUsername = authUserRepository.findByUsername(entity.getUsername());
            if (byUsername.isEmpty()) {
                authUserRepository.save(entity);
                TokenRefreshResponse response = authUserDetailsService.generateToken(new LoginDto(registerDto.getUsername(), registerDto.getPassword()));
                return new WebResponse<>(new DataDTO<>(response));
            } else return new WebResponse<>(new ErrorDTO("User already exist", 409));
        } else return new WebResponse<>(new ErrorDTO("Username is null", 400));
    }

}
