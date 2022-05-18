package com.sankore.bank.exceptions;

import com.sankore.bank.dtos.response.ErrorResponse;
import com.sankore.bank.dtos.response.TransferNotValidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;

import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> resolveUnknownHostException(Exception ex, WebRequest request) {

        if (ex instanceof UnknownHostException){
            return this.resolveUnknownHostException((UnknownHostException) ex, request);
        }
        if (ex instanceof HttpClientErrorException.Forbidden){
            return this.resolveAccessForbiddenException((HttpClientErrorException.Forbidden) ex, request);
        }
        if (ex instanceof AccountNotFoundException){
            return this.resolveAccountNotFoundException((AccountNotFoundException) ex, request);
        }
        if (ex instanceof TransferNotValidException){
            return this.resolveTransactionNotValidException((TransferNotValidException) ex, request);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnknownHostException.class)
    public final ResponseEntity<ErrorResponse> resolveUnknownHostException(UnknownHostException ex, WebRequest request) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_GATEWAY.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public final ResponseEntity<ErrorResponse> resolveAccessForbiddenException(
            HttpClientErrorException.Forbidden ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.FORBIDDEN.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<ErrorResponse> resolveAccountNotFoundException(
            AccountNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.FORBIDDEN.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TransferNotValidException.class)
    public final ResponseEntity<ErrorResponse> resolveTransactionNotValidException(
            TransferNotValidException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

}
