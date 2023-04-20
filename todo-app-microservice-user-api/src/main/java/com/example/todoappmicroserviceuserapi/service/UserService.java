package com.example.todoappmicroserviceuserapi.service;



import com.example.todoappmicroserviceuserapi.config.security.AuthUserDetails;
import com.example.todoappmicroserviceuserapi.model.AuthUser;
import com.example.todoappmicroserviceuserapi.payload.UpdateUserDto;
import com.example.todoappmicroserviceuserapi.repository.AuthUserRepository;
import com.example.todoappmicroserviceuserapi.response.DataDTO;
import com.example.todoappmicroserviceuserapi.response.ErrorDTO;
import com.example.todoappmicroserviceuserapi.response.WebResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service

public class UserService {
    private final AuthUserRepository authUserRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService(AuthUserRepository authUserRepository, @Lazy BCryptPasswordEncoder encoder) {
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
    }


    public AuthUser getCurrentUser() {
        AuthUserDetails userDetails = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.authUser();
    }

    public WebResponse<?> editPassword(UpdateUserDto updateUserDto) {
        if (Objects.isNull(updateUserDto.password()) || updateUserDto.password().length() < 8)
            return new WebResponse<>(new ErrorDTO("Password is null or less than 8 characters", 400));
        if (Objects.isNull(updateUserDto.confirmPassword()) || !updateUserDto.confirmPassword().equals(updateUserDto.password()))
            return new WebResponse<>(new ErrorDTO("Confirm password is null or not equals to password", 400));
        AuthUser currentUser = getCurrentUser();
        if (!encoder.matches(updateUserDto.password(), currentUser.getPassword()))
            return new WebResponse<>(new ErrorDTO("Password is equal old password", 400));
        currentUser.setPassword(encoder.encode(updateUserDto.password()));
        authUserRepository.save(currentUser);
        return new WebResponse<>(new DataDTO<>("Password successfully changed"));
    }
}
