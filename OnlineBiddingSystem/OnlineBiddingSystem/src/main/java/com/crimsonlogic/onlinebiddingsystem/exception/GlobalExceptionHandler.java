package com.crimsonlogic.onlinebiddingsystem.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.crimsonlogic.onlinebiddingsystem.entity.ErrorMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e,
            WebRequest request) {
        ErrorMessage err = new ErrorMessage(HttpStatus.CONFLICT.value(), LocalDateTime.now(), e.getMessage(),
                request.getDescription(true));

        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorMessage> handleCustomerAlreadyRegisteredException(UserAlreadyRegisteredException e,
            WebRequest request) {
        ErrorMessage err = new ErrorMessage(HttpStatus.CONFLICT.value(), LocalDateTime.now(), e.getMessage(),
                request.getDescription(true));

        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorMessage err = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage(),
                request.getDescription(true));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
}
