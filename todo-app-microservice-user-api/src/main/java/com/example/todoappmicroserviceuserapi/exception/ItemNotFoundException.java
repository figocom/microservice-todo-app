package com.example.todoappmicroserviceuserapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemNotFoundException extends Throwable {
    private static final long serialVersionUID = 1L;

    public ItemNotFoundException(String message) {
        super(message);
    }


}
