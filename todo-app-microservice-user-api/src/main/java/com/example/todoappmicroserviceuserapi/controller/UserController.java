package com.example.todoappmicroserviceuserapi.controller;

import com.example.todoappmicroserviceuserapi.payload.UpdateUserDto;
import com.example.todoappmicroserviceuserapi.response.ErrorDTO;
import com.example.todoappmicroserviceuserapi.response.WebResponse;
import com.example.todoappmicroserviceuserapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {

        this.userService = userService;

    }

    @PostMapping("/editPassword")
    public ResponseEntity<WebResponse<?>> editPassword(@RequestBody UpdateUserDto updateUserDto) {
        WebResponse<?> response = userService.editPassword(updateUserDto);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);
    }

}
