package com.wayapaychat.bank.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wayapaychat.bank.usecases.dtos.response.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.net.UnknownHostException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<ErrorResponse> resolveUnknownHostException(UnknownHostException ex) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(ex.getCause().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(UnknownHostException.class)
    public Object resolveHttpClientErrorException(HttpClientErrorException ex)
            throws JsonProcessingException {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(ex.getCause().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> resolveAccessForbiddenException(
            HttpClientErrorException.Forbidden ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(ex.getStatusText());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

}
