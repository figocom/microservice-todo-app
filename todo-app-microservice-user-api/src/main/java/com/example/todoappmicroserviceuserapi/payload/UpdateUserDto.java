package com.example.todoappmicroserviceuserapi.payload;

public record UpdateUserDto(
        String username,
        String password,
        String confirmPassword
) {
}
