package com.example.spartamatching_01.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ExceptionDto {

    private String errorMessage;
    private HttpStatus httpStatus;
}
