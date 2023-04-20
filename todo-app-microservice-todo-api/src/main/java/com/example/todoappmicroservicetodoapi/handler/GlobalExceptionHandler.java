package com.example.todoappmicroservicetodoapi.handler;



import com.example.todoappmicroservicetodoapi.exception.ItemNotFoundException;
import com.example.todoappmicroservicetodoapi.response.ErrorDTO;
import com.example.todoappmicroservicetodoapi.response.WebResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<WebResponse<ErrorDTO>> handler_404(ItemNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(404)
                .body( new WebResponse<>(new ErrorDTO(e.getMessage(), 404)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
