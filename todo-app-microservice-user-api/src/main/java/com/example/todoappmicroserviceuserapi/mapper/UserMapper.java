package com.example.todoappmicroserviceuserapi.mapper;


import com.example.todoappmicroserviceuserapi.model.AuthUser;
import com.example.todoappmicroserviceuserapi.payload.RegisterDto;
import com.example.todoappmicroserviceuserapi.payload.UpdateUserDto;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper implements BaseMapper<AuthUser, RegisterDto, RegisterDto, UpdateUserDto> {

    private final BCryptPasswordEncoder encoder;

    public UserMapper( @Lazy BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public AuthUser fromCreateDTO(@NonNull RegisterDto dto) {
        return AuthUser.builder().username(dto.getUsername()).
                            password(encoder.encode(dto.getPassword())).
                            name(dto.getName()).
                            build();
    }

    @Override
    public AuthUser fromUpdateDTO(@NonNull UpdateUserDto dto) {
        return null;
    }

    @Override
    public RegisterDto toDTO(@NonNull AuthUser domain) {
        return null;
    }

    @Override
    public List<RegisterDto> toDTOs(@NonNull List<AuthUser> domain) {
        return null;
    }
}
