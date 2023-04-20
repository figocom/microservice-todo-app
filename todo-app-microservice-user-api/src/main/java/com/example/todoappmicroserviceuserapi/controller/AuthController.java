package com.example.todoappmicroserviceuserapi.controller;


import com.example.todoappmicroserviceuserapi.config.security.AuthUserDetails;
import com.example.todoappmicroserviceuserapi.payload.LoginDto;
import com.example.todoappmicroserviceuserapi.payload.RegisterDto;
import com.example.todoappmicroserviceuserapi.response.DataDTO;
import com.example.todoappmicroserviceuserapi.response.ErrorDTO;
import com.example.todoappmicroserviceuserapi.response.WebResponse;
import com.example.todoappmicroserviceuserapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<WebResponse<?>> login(@Valid @RequestBody LoginDto loginDto) {
        WebResponse<?> login = authService.login(loginDto);
        return ResponseEntity.status((login.data() instanceof ErrorDTO errorDTO) ? errorDTO.getError_code() : 200).body(login);
    }

    @PostMapping("/register")
    public ResponseEntity<WebResponse<?>> register(@Valid @RequestBody RegisterDto registerDto) {
        WebResponse<?> register = authService.register(registerDto);
        return ResponseEntity.status((register.data() instanceof ErrorDTO errorDTO) ? errorDTO.getError_code() : 201).body(register);
    }

    @PostMapping("/logout")
    public ResponseEntity<WebResponse<?>> logoutUser() {
        AuthUserDetails aud= (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        return ResponseEntity.ok(new WebResponse<>(new DataDTO<>("Log out successfully")));
    }


}
