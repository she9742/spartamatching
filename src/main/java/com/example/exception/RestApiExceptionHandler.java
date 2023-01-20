package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    //IAE
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException iae) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionDto.setErrorMessage(iae.getMessage());

        return new ResponseEntity<>(
                exceptionDto,
                HttpStatus.BAD_REQUEST
        );
    }
    //NPE
    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleApiRequestException(NullPointerException npe ){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setHttpStatus(HttpStatus.NO_CONTENT);
        exceptionDto.setErrorMessage(npe.getMessage());

        return new ResponseEntity<>(
                exceptionDto,
                HttpStatus.NO_CONTENT
        );
    }

}